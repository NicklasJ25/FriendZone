package nist.friendzone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nist.friendzone.Firebase.Database;
import nist.friendzone.Realm.RealmDatabase;
import nist.friendzone.Realm.User;

public class ConnectToPartnerActivity extends AppCompatActivity implements View.OnClickListener
{
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser user;

    private EditText emailEditText;
    private TextView partnerTextView;
    private Button deletePartnerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_partner);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        partnerTextView = (TextView) findViewById(R.id.partnerTextView);
        deletePartnerButton = (Button) findViewById(R.id.deletePartnerButton);
        Button findPartnerButton = (Button) findViewById(R.id.findPartnerButton);

        user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference myReference = database.getReference(user.getEmail().replace(".", ","));

        myReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot)
            {
                if (!dataSnapshot.hasChildren())
                {
                    MyPreferences.setPartnerSection(getBaseContext(), dataSnapshot.getValue().toString());
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference(dataSnapshot.getValue().toString());
                    reference1.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                for (DataSnapshot email : dataSnapshot.getChildren())
                                {
                                    if (!email.getKey().equals(user.getEmail()))
                                    {
                                        User user1 = new User(
                                                email.child("UserProfile").child("email").getValue().toString(),
                                                email.child("UserProfile").child("firstname").getValue().toString(),
                                                email.child("UserProfile").child("lastname").getValue().toString(),
                                                email.child("UserProfile").child("birthday").getValue().toString(),
                                                email.child("UserProfile").child("phone").getValue().toString(),
                                                email.child("UserProfile").child("profilePicture").getValue().toString()
                                        );
                                        RealmDatabase.CreateUser(user1);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {

                            }
                        });
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
                else if (dataSnapshot.child("Partner").getValue() != null)
                {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    Database database = new Database();
                                    database.MakePartners(user.getEmail().replace(".", ","), dataSnapshot.child("Partner").getValue().toString().replace(".", ","));
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    myReference.child("Partner").removeValue();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(ConnectToPartnerActivity.this);
                    String dialogString = String.format(getResources().getString(R.string.FindPartnerDialogTextView), dataSnapshot.child("Partner").getValue());
                    builder.setMessage(dialogString).setPositiveButton(getResources().getString(R.string.Yes), dialogClickListener)
                            .setNegativeButton(getResources().getString(R.string.No), dialogClickListener).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        deletePartnerButton.setOnClickListener(this);
        findPartnerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        String partnerEmail = emailEditText.getText().toString().replace(".", ",").toLowerCase();
        DatabaseReference partnerReference = database.getReference(partnerEmail);
        switch (v.getId())
        {
            case R.id.deletePartnerButton:
                emailEditText.setVisibility(View.VISIBLE);
                partnerTextView.setVisibility(View.GONE);
                deletePartnerButton.setVisibility(View.GONE);

                partnerReference.child("Partner").removeValue();
                break;
            case R.id.findPartnerButton:
                partnerTextView.setText(partnerEmail);

                emailEditText.setVisibility(View.GONE);
                partnerTextView.setVisibility(View.VISIBLE);
                deletePartnerButton.setVisibility(View.VISIBLE);


                partnerReference.child("Partner").setValue(user.getEmail());
                break;
        }
    }
}

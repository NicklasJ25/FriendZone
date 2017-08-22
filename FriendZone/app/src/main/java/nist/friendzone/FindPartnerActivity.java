package nist.friendzone;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.MessageFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nist.friendzone.Firebase.Database;

public class FindPartnerActivity extends AppCompatActivity implements View.OnClickListener
{
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser user;

    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_partner);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        Button findPartnerButton = (Button) findViewById(R.id.findPartnerButton);

        user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference myReference = database.getReference(user.getEmail().replace(".", ""));

        myReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.getValue() instanceof String)
                {
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
                else if (dataSnapshot.child("Partner") != null)
                {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    Database database = new Database();
                                    database.MakePartners(user.getEmail().replace(".", ""), dataSnapshot.child("Partner").getValue().toString().replace(".", ""));
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    myReference.child("Partner").removeValue();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                    builder.setMessage(String.format(getResources().getString(R.string.FindPartnerDialogTextView), user.getEmail())).setPositiveButton(getResources().getString(R.string.Yes), dialogClickListener)
                            .setNegativeButton(getResources().getString(R.string.No), dialogClickListener).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        findPartnerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        String partnerEmail = emailEditText.getText().toString();

        DatabaseReference partnerReference = database.getReference(partnerEmail);
        partnerReference.child("Partner").setValue(user.getEmail());
    }
}

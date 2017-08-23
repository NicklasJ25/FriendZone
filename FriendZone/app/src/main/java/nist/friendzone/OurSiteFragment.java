package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class OurSiteFragment extends Fragment
{
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private TextView myNameTextView;
    private TextView myAgeTextView;
    private ImageView myAvatarView;
    private TextView partnerNameTextView;
    private TextView partnerAgeTextView;
    private ImageView partnerAvatarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_our_site, container, false);

        myNameTextView = (TextView) view.findViewById(R.id.myNameTextView);
        myAgeTextView = (TextView) view.findViewById(R.id.myAgeTextView);
        myAvatarView = (ImageView) view.findViewById(R.id.myAvatarView);
        partnerNameTextView = (TextView) view.findViewById(R.id.partnerNameTextView);
        partnerAgeTextView = (TextView) view.findViewById(R.id.partnerAgeTextView);
        partnerAvatarView = (ImageView) view.findViewById(R.id.partnerAvatarView);

        String partnerSection = MyPreferences.getPartnerSection();
        SetUserInformation(partnerSection);

        return view;
    }

    private void SetUserInformation(final String partnerSection)
    {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(partnerSection);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.

                myNameTextView.setText(dataSnapshot.child("UserProfile").child("DisplayName").getValue().toString());
                String birthday = dataSnapshot.child("UserProfile").child("Birthday").getValue().toString();
                int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(birthday.split("/")[2]);
                myAgeTextView.setText(age + " år");
                partnerEmail[0] =  dataSnapshot.child("Partner").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        partnerEmail[0] = "mille94@live.dk";
        DatabaseReference partnerRef = FirebaseDatabase.getInstance().getReference(partnerEmail[0].replace(".", ""));
        partnerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                partnerNameTextView.setText(dataSnapshot.child("UserProfile").child("DisplayName").getValue().toString());
                String birthday = dataSnapshot.child("UserProfile").child("Birthday").getValue().toString();
                int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(birthday.split("/")[2]);
                partnerAgeTextView.setText(age + " år");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String path = user.getEmail().replace(".", "") + "/ProfilePicture.png";
        StorageReference storageReference = storage.getReference(path);
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(myAvatarView);

        String partnerPath = partnerEmail[0].replace(".", "") + "/ProfilePicture.png";
        StorageReference partnerStorageReference = storage.getReference(partnerPath);
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(partnerStorageReference)
                .into(partnerAvatarView);
    }
}

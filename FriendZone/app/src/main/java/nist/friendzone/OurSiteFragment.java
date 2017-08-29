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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

import nist.friendzone.Realm.User;
import nist.friendzone.Realm.RealmDatabase;

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

        String partnerSection = MyPreferences.getPartnerSection(getContext());
        SetUserInformation(partnerSection);

        return view;
    }

    private void SetUserInformation(final String partnerSection)
    {
        final String[] emails = partnerSection.split("\\\\");
        User user = RealmDatabase.GetUser(emails[0]);
        myNameTextView.setText(user.firstname + " " + user.lastname);
        int myAge = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(user.birthday.split("/")[2]);
        myAgeTextView.setText(String.format(getResources().getString(R.string.ageTextView), myAge));


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(partnerSection);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //myNameTextView.setText(dataSnapshot.child(emails[0]).child("UserProfile").child("DisplayName").getValue().toString());
                partnerNameTextView.setText(dataSnapshot.child(emails[1]).child("UserProfile").child("DisplayName").getValue().toString());
                //String myBirthday = dataSnapshot.child(emails[0]).child("UserProfile").child("birthday").getValue().toString();
                //int myAge = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(myBirthday.split("/")[2]);
                //myAgeTextView.setText(String.format(getResources().getString(R.string.ageTextView), myAge));
                String partnerBirthday = dataSnapshot.child(emails[1]).child("UserProfile").child("birthday").getValue().toString();
                int partnerAge = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(partnerBirthday.split("/")[2]);
                partnerAgeTextView.setText(String.format(getResources().getString(R.string.ageTextView), partnerAge));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        String myPath = emails[0] + "/profilePicture.png";
        StorageReference myStorageReference = storage.getReference(myPath);
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(myStorageReference)
                .into(myAvatarView);

        String partnerPath = emails[1] + "/profilePicture.png";
        StorageReference partnerStorageReference = storage.getReference(partnerPath);
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(partnerStorageReference)
                .into(partnerAvatarView);
    }
}

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

import static android.content.ContentValues.TAG;

public class OurSiteFragment extends Fragment
{
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private TextView myNameTextView;
    private TextView myAgeTextView;
    private ImageView myAvatarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_our_site, container, false);

        myNameTextView = (TextView) view.findViewById(R.id.myNameTextView);
        myAgeTextView = (TextView) view.findViewById(R.id.myAgeTextView);
        myAvatarView = (ImageView) view.findViewById(R.id.myAvatarView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        SetUserInformation(user.getEmail());

        return view;
    }

    private void SetUserInformation(String email)
    {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(email);
        myRef.child("UserProfile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myNameTextView.setText(dataSnapshot.child("DisplayName").getValue().toString());
                String birthday = dataSnapshot.child("Birthday").getValue().toString();
                int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(birthday.split("/")[2]);
                myAgeTextView.setText(age + " år");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String path = user.getUid() + "/ProfilePicture.png";
        StorageReference storageReference = storage.getReference(path);
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(myAvatarView);
    }
}

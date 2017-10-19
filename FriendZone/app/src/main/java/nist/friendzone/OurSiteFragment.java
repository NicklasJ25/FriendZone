package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

import nist.friendzone.Model.RealmDatabase;
import nist.friendzone.Model.User;

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

        myNameTextView = view.findViewById(R.id.myNameTextView);
        myAgeTextView = view.findViewById(R.id.myAgeTextView);
        myAvatarView = view.findViewById(R.id.myAvatarView);
        partnerNameTextView = view.findViewById(R.id.partnerNameTextView);
        partnerAgeTextView = view.findViewById(R.id.partnerAgeTextView);
        partnerAvatarView = view.findViewById(R.id.partnerAvatarView);

        SetUserInformation();

        return view;
    }

    private void SetUserInformation()
    {
        String email = MyPreferences.getLoggedInEmail(getContext());
        User user = RealmDatabase.GetUser(email);

        myNameTextView.setText(user.Firstname + " " + user.Lastname);
        myAgeTextView.setText(User.GetAge(user.Birthday));

        partnerNameTextView.setText(user.User2.Firstname + " " + user.User2.Lastname);
        partnerAgeTextView.setText(User.GetAge(user.User2.Birthday));

        StorageReference myStorageReference = storage.getReferenceFromUrl(user.ProfilePicture);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(myStorageReference)
                .into(myAvatarView);

        StorageReference partnerStorageReference = storage.getReferenceFromUrl(user.User2.ProfilePicture);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(partnerStorageReference)
                .into(partnerAvatarView);
    }
}

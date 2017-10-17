package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

import nist.friendzone.Realm.RealmDatabase;
import nist.friendzone.Realm.User;

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

        String partnerSection = MyPreferences.getLoggedInEmail(getContext());
        SetUserInformation(partnerSection);

        return view;
    }

    private void SetUserInformation(final String partnerSection)
    {
        String myEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        User myUser = RealmDatabase.GetUser(myEmail);
        myNameTextView.setText(myUser.Firstname + " " + myUser.Lastname);
        int myAge = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(myUser.Birthday.split("/")[2]);
        myAgeTextView.setText(String.format(getResources().getString(R.string.ageTextView), myAge));

        String partnerEmail = partnerSection.replace(myEmail.replace(".", ","), "").replace("\\", "").replace(",", ".");
        User partnerUser = RealmDatabase.GetUser(partnerEmail);
        partnerNameTextView.setText(partnerUser.Firstname + " " + partnerUser.Lastname);
        int partnerAge = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(partnerUser.Birthday.split("/")[2]);
        partnerAgeTextView.setText(String.format(getResources().getString(R.string.ageTextView), partnerAge));

        StorageReference myStorageReference = storage.getReferenceFromUrl(myUser.ProfilePicture);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(myStorageReference)
                .into(myAvatarView);

        StorageReference partnerStorageReference = storage.getReferenceFromUrl(partnerUser.ProfilePicture);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(partnerStorageReference)
                .into(partnerAvatarView);
    }
}

package nist.friendzone;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nist.friendzone.Realm.RealmDatabase;
import nist.friendzone.Realm.User;

public class CreatePostFragment extends Fragment implements View.OnClickListener
{
    FirebaseDatabase database;

    EditText descriptionEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        descriptionEditText = (EditText) view.findViewById(R.id.descriptionEditText);
        Button publishButton = (Button) view.findViewById(R.id.createPostButton);

        publishButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        String description = descriptionEditText.getText().toString();
        createNewsfeed(description);
    }

    private void createNewsfeed(final String description)
    {
        final String partnerSection = MyPreferences.getPartnerSection(getContext());
        database = FirebaseDatabase.getInstance();
        String myEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        User myUser = RealmDatabase.GetUser(myEmail);
        String partnerEmail = partnerSection.replace(myEmail.replace(".", ","), "").replace("\\", "").replace(",", ".");
        User partnerUser = RealmDatabase.GetUser(partnerEmail);

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String date = dateFormat.format(new Date());
        String time = timeFormat.format(new Date());
        DatabaseReference newsfeed = database.getReference("Newsfeed").child(date).child(time + partnerSection);
        newsfeed.child("partnerSection").setValue(partnerSection);
        newsfeed.child("part1Picture").setValue(myUser.profilePicture);
        newsfeed.child("part2Picture").setValue(partnerUser.profilePicture);
        newsfeed.child("names").setValue(myUser.firstname + " & " + partnerUser.firstname);
        int myAge = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(myUser.birthday.split("/")[2]);
        int partnerAge = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(partnerUser.birthday.split("/")[2]);
        newsfeed.child("ages").setValue(myAge + " & " + partnerAge);
        newsfeed.child("description").setValue(description);

        BottomNavigationView navigation = (BottomNavigationView)getActivity().findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.newsFeedNavigation);
    }
}

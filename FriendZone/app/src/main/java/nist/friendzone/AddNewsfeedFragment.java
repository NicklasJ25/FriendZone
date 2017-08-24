package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewsfeedFragment extends Fragment
{
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_add_newsfeed, container, false);



        return view;
    }

    private void createNewsfeed(final String description)
    {
        final String partnerSection = MyPreferences.getPartnerSection(getContext());
        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(partnerSection);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                final String[] emails = partnerSection.split("\\\\");

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String date = dateFormat.format(new Date());
                DatabaseReference newsfeed = database.getReference("Newsfeed").child(date);
                newsfeed.child(partnerSection).child("ProfilePicture1").setValue(dataSnapshot.child(emails[0]).child("ProfilePicture").getValue());
                newsfeed.child(partnerSection).child("ProfilePicture2").setValue(dataSnapshot.child(emails[1]).child("ProfilePicture").getValue());
                newsfeed.child(date).child(partnerSection).child("Names").setValue("Camilla & Nicklas");
                newsfeed.child(partnerSection).child("Ages").setValue("23 & 24");
                newsfeed.child(partnerSection).child("Description").setValue("Nogen der kunne tænke sig at lave noget mad sammen? :D");
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}

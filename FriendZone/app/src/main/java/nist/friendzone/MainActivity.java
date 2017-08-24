package nist.friendzone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.newsFeedNavigation);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else if (MyPreferences.getPartnerSection(getBaseContext()).equals(""))
                {
                    final DatabaseReference databaseReference = database.getReference(user.getEmail().replace(".", ""));
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            MyPreferences.setPartnerSection(getBaseContext(), dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {

                        }
                    });
                }
            }
        };

//        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        String date = dateFormat.format(new Date());
//        String partnerSection = MyPreferences.getPartnerSection(this);
//        database.getReference().child("Newsfeed").child(date).child(partnerSection).child("ProfilePicture1").setValue("https://firebasestorage.googleapis.com/v0/b/friendzone-5ecba.appspot.com/o/mille94%40livedk%2FProfilePicture.png?alt=media&token=7d7e31c2-0df7-4562-bf17-557e00f8adff");
//        database.getReference().child("Newsfeed").child(date).child(partnerSection).child("ProfilePicture2").setValue("https://firebasestorage.googleapis.com/v0/b/friendzone-5ecba.appspot.com/o/nicklasj25%40hotmailcom%2FProfilePicture.png?alt=media&token=7b9bb8ee-2aa8-4c14-a8a4-ea9cab3c32df");
//        database.getReference().child("Newsfeed").child(date).child(partnerSection).child("Names").setValue("Camilla & Nicklas");
//        database.getReference().child("Newsfeed").child(date).child(partnerSection).child("Ages").setValue("23 & 24");
//        database.getReference().child("Newsfeed").child(date).child(partnerSection).child("Description").setValue("Nogen der kunne tænke sig at lave noget mad sammen? :D");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.newsFeedNavigation:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, new NewsFeedFragment())
                        .commit();
                return true;
            case R.id.navigation_dashboard:

                return true;
            case R.id.oursiteNavigation:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, new OurSiteFragment())
                        .commit();
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.signOutMenu:
                FirebaseAuth.getInstance().signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}

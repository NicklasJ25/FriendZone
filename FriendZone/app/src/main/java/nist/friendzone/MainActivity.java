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

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.newsFeedNavigation);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else if (MyPreferences.getPartnerSection(getBaseContext()).equals(""))
                {
                    Intent intent = new Intent(getBaseContext(), ConnectToPartnerActivity.class);
                    startActivity(intent);
                }
            }
        };
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
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, new AddNewsfeedFragment())
                        .commit();
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
        switch (item.getItemId()) {
            case R.id.signOutMenu:
                MyPreferences.ClearPrefrences(this);
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
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

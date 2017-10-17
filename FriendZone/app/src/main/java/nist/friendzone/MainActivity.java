package nist.friendzone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import nist.friendzone.Model.RealmDatabase;
import nist.friendzone.Model.User;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.newsFeedNavigation);

        String email = MyPreferences.getLoggedInEmail(this);
        if (email == null)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        User user = RealmDatabase.GetUser(email);
        if (user.User2 == null || user.User2.Partner == null)
        {
            Intent intent = new Intent(this, ConnectToPartnerActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.newsFeedNavigation:
                Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("NewsFeedFragment");
                if (fragment1 == null || !fragment1.isVisible())
                {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, new NewsFeedFragment(), "NewsFeedFragment")
                            .commit();
                }
                return true;
            case R.id.createPostNavigation:
                Fragment fragment2 = getSupportFragmentManager().findFragmentByTag("CreatePostFragment");
                if (fragment2 == null || !fragment2.isVisible())
                {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, new CreatePostFragment(), "CreatePostFragment")
                            .commit();
                }
                return true;
            case R.id.oursiteNavigation:
                Fragment fragment3 = getSupportFragmentManager().findFragmentByTag("OurSiteFragment");
                if (fragment3 == null || !fragment3.isVisible())
                {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, new OurSiteFragment(), "OurSiteFragment")
                            .commit();
                }
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
                RealmDatabase.ClearDatabase();

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package nist.friendzone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new LoginFragment())
                .commit();
    }
}


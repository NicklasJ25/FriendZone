package nist.friendzone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import nist.friendzone.Login.EmailPassword;

public class LoginActivity extends AppCompatActivity implements OnClickListener
{
    public TextView emailTextView;
    private EditText passwordTextView;
    public ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginProgressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        passwordTextView = (EditText) findViewById(R.id.passwordTextView);
    }

    @Override
    public void onClick(View v)
    {
        EmailPassword emailPassword = new EmailPassword(this);
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        switch (v.getId())
        {
            case R.id.loginButton:
                emailPassword.LoginUser(email, password);
                break;
            case R.id.createUserButton:
                emailPassword.CreateUser(email,password);
                break;
        }
    }
}


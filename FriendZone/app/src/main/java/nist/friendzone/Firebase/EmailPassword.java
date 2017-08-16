package nist.friendzone.Firebase;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import nist.friendzone.R;

import static android.content.ContentValues.TAG;

public class EmailPassword implements OnCompleteListener<Void>
{
    private FragmentActivity context;

    private FirebaseAuth firebaseAuth;

    public EmailPassword(FragmentActivity context)
    {
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void CreateUser(String email, String password, final String firstname, final String lastname, final String birthday, final String phone)
    {
        showProgress(true);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Database database = new Database();
                    database.UpdateUser("DisplayName", firstname + " " + lastname);
                    database.UpdateUser("Birthday", birthday);
                    database.UpdateUser("Phone", phone);
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                }
                showProgress(false);
            }
        });
    }

    public void LoginUser(String email, String password)
    {
        showProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Toast.makeText(context, "Authentication success.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });
    }

    public void UpdateUser(String key, String value)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates;

        if (user != null)
        {
            switch (key)
            {
                case "DisplayName":
                    profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(value)
                            .build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(this);
                    break;
                case "Email":
                    user.updateEmail(value).addOnCompleteListener(this);
                    break;
                case "Password":
                    user.updatePassword(value).addOnCompleteListener(this);
                    break;
                case "PhotoUri":
                    profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(Uri.parse(value))
                            .build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(this);
                    break;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        final ProgressBar loginProgressBar = (ProgressBar) context.findViewById(R.id.loginProgressBar);

        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            loginProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    loginProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        else
        {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loginProgressBar.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onComplete(@NonNull Task<Void> task)
    {
        if (task.isSuccessful()) {
            Log.d(TAG, "User updated.");
        }
    }
}

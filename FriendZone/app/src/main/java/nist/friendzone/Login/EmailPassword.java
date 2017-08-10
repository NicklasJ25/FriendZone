package nist.friendzone.Login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nist.friendzone.LoginActivity;

import static android.content.ContentValues.TAG;

public class EmailPassword
{
    private LoginActivity context;

    private FirebaseAuth mAuth;

    public EmailPassword(LoginActivity context)
    {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }

    public void CreateUser(String email, String password)
    {
        showProgress(true);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
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
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

            context.loginProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            context.loginProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    context.loginProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else
        {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            context.loginProgressBar.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

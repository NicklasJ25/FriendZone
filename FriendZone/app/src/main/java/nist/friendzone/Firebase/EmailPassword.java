package nist.friendzone.Firebase;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.realm.Realm;
import nist.friendzone.R;
import nist.friendzone.Realm.User;

import static android.content.ContentValues.TAG;

public class EmailPassword
{
    private FragmentActivity context;
    private FirebaseAuth firebaseAuth;
    private Realm realm;

    public EmailPassword(FragmentActivity context)
    {
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
        realm = Realm.getDefaultInstance();
    }

    public void CreateUser(final User user, String password)
    {
        showProgress(true);
        firebaseAuth.createUserWithEmailAndPassword(user.email, password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    realm.beginTransaction();
                    realm.copyToRealm(user);
                    realm.commitTransaction();

                    Database database = new Database();
                    database.UpdateUser("UserProfile/email", user.email);
                    database.UpdateUser("UserProfile/firstname", user.firstname);
                    database.UpdateUser("UserProfile/lastname", user.lastname);
                    database.UpdateUser("UserProfile/phone", user.phone);
                    database.UpdateUser("UserProfile/birthday", user.birthday);

                    if (user.profilePicture != null)
                    {
                        Uri profilePictureUri = Uri.parse(user.profilePicture);
                        database.UploadProfilePicture(profilePictureUri);
                    }
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                }
                else
                {
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
                }
                else
                {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                }
                showProgress(false);
            }
        });
    }

    private void showProgress(final boolean show)
    {
        final ProgressBar loginProgressBar = (ProgressBar) context.findViewById(R.id.loginProgressBar);
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
}

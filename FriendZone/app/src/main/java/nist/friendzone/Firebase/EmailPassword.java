package nist.friendzone.Firebase;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nist.friendzone.FindPartnerActivity;
import nist.friendzone.MyPreferences;
import nist.friendzone.R;

import static android.content.ContentValues.TAG;

public class EmailPassword
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
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (MyPreferences.getPartnerSection(context).equals(""))
                    {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(user.getEmail().replace(".", ""));
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                if (dataSnapshot.hasChildren())
                                {
                                    Intent intent = new Intent(context, FindPartnerActivity.class);
                                    context.startActivity(intent);
                                } else
                                {
                                    MyPreferences.setPartnerSection(context, dataSnapshot.getValue().toString());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {

                            }
                        });
                    }
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
}

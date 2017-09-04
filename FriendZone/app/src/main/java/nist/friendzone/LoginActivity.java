package nist.friendzone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.realm.Realm;
import nist.friendzone.Realm.RealmDatabase;
import nist.friendzone.Realm.User;

public class LoginActivity extends AppCompatActivity
{
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new LoginFragment())
                .commit();

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(user.getEmail().replace(".", ","));
                    reference.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.hasChildren())
                            {
                                User user1 = new User(
                                        dataSnapshot.child("UserProfile").child("email").getValue().toString(),
                                        dataSnapshot.child("UserProfile").child("firstname").getValue().toString(),
                                        dataSnapshot.child("UserProfile").child("lastname").getValue().toString(),
                                        dataSnapshot.child("UserProfile").child("birthday").getValue().toString(),
                                        dataSnapshot.child("UserProfile").child("phone").getValue().toString(),
                                        dataSnapshot.child("UserProfile").child("profilePicture").getValue().toString()
                                );
                                RealmDatabase.CreateUser(user1);
                            }
                            else
                            {
                                MyPreferences.setPartnerSection(getBaseContext(), dataSnapshot.getValue().toString());
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference(dataSnapshot.getValue().toString()).child(user.getEmail().replace(".", ",")).child("UserProfile");
                                reference1.addListenerForSingleValueEvent(new ValueEventListener()
                                {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot)
                                    {
                                        User user1 = new User(
                                                dataSnapshot.child("email").getValue().toString(),
                                                dataSnapshot.child("firstname").getValue().toString(),
                                                dataSnapshot.child("lastname").getValue().toString(),
                                                dataSnapshot.child("birthday").getValue().toString(),
                                                dataSnapshot.child("phone").getValue().toString(),
                                                dataSnapshot.child("profilePicture").getValue().toString()
                                        );
                                        RealmDatabase.CreateUser(user1);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError)
                                    {

                                    }
                                });
                            }
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {

                        }
                    });
                }
            }
        };
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


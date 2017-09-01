package nist.friendzone.Firebase;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.realm.Realm;
import nist.friendzone.Realm.User;

import static android.content.ContentValues.TAG;

public class Database
{
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseDatabase database;
    private Realm realm = Realm.getDefaultInstance();

    public Database()
    {
        database = FirebaseDatabase.getInstance();
    }

    public void UpdateUser(String key, Object value)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail().replace(".", "");
        DatabaseReference reference = database.getReference().child(email).child(key);
        reference.setValue(value);
    }

    public void UploadProfilePicture(Uri profilePicture)
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String path = user.getEmail().replace(".", "") + "/profilePicture.png";

        StorageReference storageReference = storage.getReference(path);
        UploadTask uploadTask = storageReference.putFile(profilePicture);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                UpdateUser("UserProfile/profilePicture", downloadUrl.toString());

                realm.beginTransaction();
                User updateUser = realm.where(User.class).equalTo("email", user.getEmail()).findFirst();
                updateUser.profilePicture = downloadUrl.toString();
                realm.copyToRealmOrUpdate(updateUser);
                realm.commitTransaction();
            }
        });
    }

    public void MakePartners(final String myEmail, final String partnerEmail)
    {
        try
        {
            final DatabaseReference databaseReference = database.getReference();
            final String partnerSection = myEmail + "\\" + partnerEmail;
            databaseReference.child(myEmail).addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    databaseReference.child(myEmail).child("Partner").removeValue();
                    databaseReference.child(partnerSection).child(myEmail).setValue(dataSnapshot.getValue());
                    databaseReference.child(myEmail).setValue(partnerSection);
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Log.e(TAG, "First email failed to move to partner section");
                }
            });

            databaseReference.child(partnerEmail).addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    databaseReference.child(partnerSection).child(partnerEmail).setValue(dataSnapshot.getValue());
                    databaseReference.child(partnerEmail).setValue(partnerSection);
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Log.e(TAG, "Second email failed to move to partner section");
                }
            });
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Failed to move emails");
        }
    }
}

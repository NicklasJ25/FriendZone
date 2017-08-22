package nist.friendzone.Firebase;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Database
{
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseDatabase database;

    public Database()
    {
        database = FirebaseDatabase.getInstance();
    }

    public void UpdateUser(String key, Object value)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail().replace(".", "");
        DatabaseReference myRef = database.getReference(email);
        myRef.child("UserProfile").child(key).setValue(value);
    }

    public void UploadProfilePicture(Uri profilePicture)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String path = user.getEmail().replace(".", "") + "/ProfilePicture.png";

        StorageReference storageReference = storage.getReference(path);
        UploadTask uploadTask = storageReference.putFile(profilePicture);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                UpdateUser("ProfilePicture", downloadUrl.toString());
            }
        });
    }
}

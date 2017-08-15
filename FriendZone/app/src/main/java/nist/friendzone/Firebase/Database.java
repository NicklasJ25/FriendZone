package nist.friendzone.Firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database
{
    private FirebaseDatabase database;

    public Database()
    {
        database = FirebaseDatabase.getInstance();
    }

    public void UpdateUser(String key, String value)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference myRef = database.getReference(uid);
        myRef.child("UserProfile").child(key).setValue(value);
    }

//    public String GetUserInformation(String uid, String key)
//    {
//
//    }
}

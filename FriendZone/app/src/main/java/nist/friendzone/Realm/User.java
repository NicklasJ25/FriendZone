package nist.friendzone.Realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject
{
    @PrimaryKey
    public String email;
    public String firstname;
    public String lastname;
    public String birthday;
    public String phone;
    public String profilePicture;

    public User()
    {

    }

    public User(String email, String firstname, String lastname, String birthday, String phone, String profilePicture)
    {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
        this.phone = phone;
        this.profilePicture = profilePicture;
    }
}


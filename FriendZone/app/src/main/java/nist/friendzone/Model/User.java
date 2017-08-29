package nist.friendzone.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject
{

    @PrimaryKey
    public String email;
    public String firstname;
    public String lastname;
    public String birthday;
    public String picturePath;

    public User()
    {

    }

    public User(String email, String firstname, String lastname, String birthday, String picturePath)
    {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
        this.picturePath = picturePath;
    }
}

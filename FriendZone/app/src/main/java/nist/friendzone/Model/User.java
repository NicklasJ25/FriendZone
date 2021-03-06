package nist.friendzone.Model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject
{
    @PrimaryKey
    public String Email;
    public String Firstname;
    public String Lastname;
    public Date Birthday;
    public String Phone;
    public String Streetname;
    public String Postalcode;
    public String ProfilePicture;
    public String Password;
    public String Partner;
    public User User2;

    public User()
    {

    }

    public User(String Email, String Firstname, String Lastname, Date Birthday, String Phone, String Streetname, String Postalcode, String ProfilePicture, String Password)
    {
        this.Email = Email;
        this.Firstname = Firstname;
        this.Lastname = Lastname;
        this.Birthday = Birthday;
        this.Phone = Phone;
        this.Streetname = Streetname;
        this.Postalcode = Postalcode;
        this.ProfilePicture = ProfilePicture;
        this.Password = Password;
    }

    public static int GetAge(Date birthday)
    {
        Date now = new Date();
        long birthdayLong = birthday.getTime();
        long nowLong = now.getTime();
        long difference = nowLong - birthdayLong;
        birthday.setTime(difference);
        int age = birthday.getYear() - 70;
        return age;
    }
}


package nist.friendzone.Model;

import java.util.Calendar;
import java.util.Date;

public class Post
{
    public int ID;
    public String Email;
    public String Description;
    public Date Time;
    public User User;

    public Post()
    {

    }

    public Post(String Email, String Description, Date Time)
    {
        this.Email = Email;
        this.Description = Description;
        this.Time = Time;
    }
}

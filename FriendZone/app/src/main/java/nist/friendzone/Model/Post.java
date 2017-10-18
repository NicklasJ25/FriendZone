package nist.friendzone.Model;

import java.util.Calendar;

public class Post
{
    public int ID;
    public String Email;
    public String Description;
    public Calendar Time;
    public User User;

    public Post()
    {

    }

    public Post(String Email, String Description, Calendar Time)
    {
        this.Email = Email;
        this.Description = Description;
        this.Time = Time;
    }
}

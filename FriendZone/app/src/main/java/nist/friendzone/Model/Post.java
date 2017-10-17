package nist.friendzone.Model;

import java.util.Date;

public class Post
{
    public String Email;
    public String Description;
    public Date Time;
    public User User1;
    public User User2;

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

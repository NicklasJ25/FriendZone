package nist.friendzone.Model;

import java.util.Calendar;
import java.util.Date;

public class Comment
{
    public int ID;
    public int PostID;
    public String Email;
    public String Description;
    public Date Time;
    public Post Post;
    public User User;

    public Comment()
    {

    }

    public Comment(int PostID, String Email, String Description, Date Time)
    {
        this.PostID = PostID;
        this.Email = Email;
        this.Description = Description;
        this.Time = Time;
    }
}

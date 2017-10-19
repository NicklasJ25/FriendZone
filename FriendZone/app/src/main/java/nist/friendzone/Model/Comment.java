package nist.friendzone.Model;

import java.util.Calendar;

public class Comment
{
    public int ID;
    public int PostID;
    public String Email;
    public String Description;
    public Calendar Time;
    public Post Post;
    public User User;

    public Comment()
    {

    }

    public Comment(int PostID, String Email, String Description, Calendar Time)
    {
        this.PostID = PostID;
        this.Email = Email;
        this.Description = Description;
        this.Time = Time;
    }
}

package nist.friendzone.Model;

public class Comment
{
    public int ID;
    public int PostID;
    public String Email;
    public String Description;
    public String Time;
    public Post Post;
    public User User;

    public Comment()
    {

    }

    public Comment(int PostID, String Email, String Description, String Time)
    {
        this.PostID = PostID;
        this.Email = Email;
        this.Description = Description;
        this.Time = Time;
    }
}

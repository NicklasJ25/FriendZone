package nist.friendzone.Model;

public class User
{
    public String picturePath;
    public String name;
    public int age;
    public String description;

    public User(String picturePath, String name, int age, String descrition)
    {
        this.picturePath = picturePath;
        this.name = name;
        this.age = age;
        this.description = descrition;
    }
}

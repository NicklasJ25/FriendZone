package nist.friendzone.Model;

public class Newsfeed
{
    public String firebaseRef;
    public String part1Picture;
    public String part2Picture;
    public String names;
    public String ages;
    public String description;
    public String time;

    public Newsfeed()
    {

    }

    public Newsfeed(String firebaseRef, String part1Picture, String part2Picture, String names, String ages, String description, String time)
    {
        this.firebaseRef = firebaseRef;
        this.part1Picture = part1Picture;
        this.part2Picture = part2Picture;
        this.names = names;
        this.ages = ages;
        this.description = description;
        this.time = time;
    }
}

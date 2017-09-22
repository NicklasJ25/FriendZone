package nist.friendzone.Model;

public class Comment
{
    public String part1Picture;
    public String part2Picture;
    public String names;
    public String ages;
    public String description;
    public String time;

    public Comment()
    {

    }

    public Comment(String part1Picture, String part2Picture, String names, String ages, String description, String time)
    {
        this.part1Picture = part1Picture;
        this.part2Picture = part2Picture;
        this.names = names;
        this.ages = ages;
        this.description = description;
        this.time = time;
    }
}

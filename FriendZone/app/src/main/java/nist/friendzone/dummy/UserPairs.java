package nist.friendzone.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPairs
{
    public static final List<UserPair> userPairs = new ArrayList<>();

    private static final int COUNT = 10;

    static
    {
        for (int i = 1; i <= COUNT; i++)
        {
            User user1 = new User("Nicklas", 24, "Elsker at spille PS4");
            User user2 = new User("Camilla", 23, "Elsker at spille Ipad");
            addItem(createDummyItem(user1, user2));
        }
    }

    private static void addItem(UserPair userPair)
    {
        userPairs.add(userPair);
    }

    private static UserPair createDummyItem(User user1, User user2)
    {
        return new UserPair(user1.name + " & " + user2.name, user1.age + " & " + user2.age, user1.description + " & " + user2.description);
    }

    public static class UserPair
    {
        public final String names;
        public final String ages;
        public final String descriptions;

        public UserPair(String names, String ages, String descriptions)
        {
            this.names = names;
            this.ages = ages;
            this.descriptions = descriptions;
        }
    }
}

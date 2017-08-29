package nist.friendzone.Realm;

import io.realm.Realm;

public class RealmDatabase
{
    private static Realm realm = Realm.getDefaultInstance();

    public static boolean UserExists(String email)
    {
        User user = realm.where(User.class).equalTo("email", email).findFirst();

        if (user == null)
        {
            return false;
        }

        return true;
    }

    public static void CreateUser(String email, String firstname, String lastname, String birthday, String phone, String picturePath)
    {
        realm.beginTransaction();
        User user = new User(email, firstname, lastname, birthday, phone, picturePath);
        realm.copyToRealm(user);
        realm.commitTransaction();
    }

    public static void CreateUser(User user)
    {
        realm.beginTransaction();
        realm.copyToRealm(user);
        realm.commitTransaction();
    }

    public static User GetUser(String email)
    {
        User user = realm.where(User.class).equalTo("email", email).findFirst();
        return user;
    }
}

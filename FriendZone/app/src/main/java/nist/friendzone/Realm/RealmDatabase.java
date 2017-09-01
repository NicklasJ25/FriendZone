package nist.friendzone.Realm;

import io.realm.Realm;

public class RealmDatabase
{
    public static boolean UserExists(String email)
    {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("email", email).findFirst();

        if (user == null)
        {
            return false;
        }

        return true;
    }

    public static void CreateUser(String email, String firstname, String lastname, String birthday, String phone, String picturePath)
    {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        User user = new User(email, firstname, lastname, birthday, phone, picturePath);
        realm.copyToRealm(user);
        realm.commitTransaction();
    }

    public static void CreateUser(User user)
    {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(user);
        realm.commitTransaction();
    }

    public static User GetUser(String email)
    {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("email", email).findFirst();
        return user;
    }
}
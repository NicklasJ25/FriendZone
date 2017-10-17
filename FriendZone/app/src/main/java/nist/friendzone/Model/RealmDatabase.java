package nist.friendzone.Model;

import io.realm.Realm;

public class RealmDatabase
{
    public static boolean UserExists(String email)
    {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("Email", email).findFirst();

        if (user == null)
        {
            return false;
        }

        return true;
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
        User user = realm.where(User.class).equalTo("Email", email).findFirst();
        return user;
    }

    public static void ClearDatabase()
    {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }
}

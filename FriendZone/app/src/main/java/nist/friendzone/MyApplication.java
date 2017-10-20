package nist.friendzone;

import android.app.Application;

import io.realm.Realm;

public class MyApplication extends Application
{
    //public static String baseUrl = "http://10.216.10.130/FriendZone/api/"; //Hjemme
    public static String baseUrl = "http://10.132.206.131/FriendZone/api/"; //Netcompany cellular
    //public static String baseUrl = "http://0.0.0.0/FriendZone/api/"; //NNE

    @Override
    public void onCreate()
    {
        super.onCreate();
        Realm.init(this);
    }
}

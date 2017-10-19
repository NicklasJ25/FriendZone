package nist.friendzone;

import android.app.Application;

import io.realm.Realm;

public class MyApplication extends Application
{
    public static String baseUrl = "http://10.216.10.118/FriendZone/api/";

    @Override
    public void onCreate()
    {
        super.onCreate();
        Realm.init(this);
    }
}

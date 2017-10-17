package nist.friendzone;

import android.content.Context;
import android.preference.PreferenceManager;

public class MyPreferences
{
    private static String LOGGED_IN_EMAIL = "LOGGED_IN_EMAIL";

    public static void setLoggedInEmail(Context context, String loggedInEmail)
    {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(LOGGED_IN_EMAIL, loggedInEmail)
                .apply();
    }

    public static String getLoggedInEmail(Context context)
    {
        String loggedInEmail = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(LOGGED_IN_EMAIL, null);
        return loggedInEmail;
    }

    public static void ClearPrefrences(Context context)
    {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .clear()
                .apply();
    }
}

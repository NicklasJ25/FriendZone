package nist.friendzone;

import android.content.Context;
import android.preference.PreferenceManager;

public class MyPreferences
{
    private static String PARTNER_SECTION_FIREBASE = "PARTNER_SECTION_FIREBASE";

    public static void setPartnerSection(Context context, String partnerSection)
    {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PARTNER_SECTION_FIREBASE, partnerSection)
                .apply();
    }

    public static String getPartnerSection(Context context)
    {
        String partnerSection = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PARTNER_SECTION_FIREBASE, "");
        return partnerSection;
    }

    public static void ClearPrefrences(Context context)
    {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .clear()
                .apply();
    }
}

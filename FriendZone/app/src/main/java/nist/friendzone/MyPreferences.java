package nist.friendzone;

import android.content.Context;
import android.preference.PreferenceManager;

public class MyPreferences
{
    private static String PARTNER_SECTION_FIREBASE = "PARTNER_SECTION_FIREBASE";

    public static void setPartnerSection(String partnerSection)
    {
        PreferenceManager.getDefaultSharedPreferences(null)
                .edit()
                .putString(PARTNER_SECTION_FIREBASE, partnerSection)
                .apply();
    }

    public static String getPartnerSection()
    {
        String partnerSection = PreferenceManager.getDefaultSharedPreferences(null)
                .getString(PARTNER_SECTION_FIREBASE, "");
        return partnerSection;
    }
}

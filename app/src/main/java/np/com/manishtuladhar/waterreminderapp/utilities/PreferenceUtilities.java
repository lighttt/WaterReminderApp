package np.com.manishtuladhar.waterreminderapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtilities {

    public static final String KEY_WATER_COUNT = "water-count";
    public static final String KEY_CHARGING_REMINDER_COUNT = "charging-reminder-count";

    private static final int DEFAULT_COUNT = 0;

    /**
     * Get the total water count
     */
    public static int getWaterCount(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int glassOfWater= prefs.getInt(KEY_WATER_COUNT,DEFAULT_COUNT);
        return glassOfWater;
    }

    /**
     * Set the water count
     */
    synchronized private static void setWaterCount(Context context, int glassOfWater)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_WATER_COUNT,glassOfWater);
        editor.apply();
    }

    /**
     * Increments water count by 1
     */
    synchronized public static void incrementWaterCount(Context context)
    {
        int waterCount = PreferenceUtilities.getWaterCount(context);
        PreferenceUtilities.setWaterCount(context,++waterCount);
    }

    /**
     * Get the total charging reminder count
     */
    public static int getChargingReminder(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingReminder= prefs.getInt(KEY_CHARGING_REMINDER_COUNT,DEFAULT_COUNT);
        return chargingReminder;
    }

    /**
     * Increment the charge reminder count by 1
     */
    synchronized public static void incrementReminderCount(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingReminder= prefs.getInt(KEY_CHARGING_REMINDER_COUNT,DEFAULT_COUNT);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_CHARGING_REMINDER_COUNT,++chargingReminder);
        editor.apply();
    }


}

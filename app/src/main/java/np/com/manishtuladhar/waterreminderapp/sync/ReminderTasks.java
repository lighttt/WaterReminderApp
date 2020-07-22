package np.com.manishtuladhar.waterreminderapp.sync;

import android.content.Context;

import np.com.manishtuladhar.waterreminderapp.utilities.NotificationUtils;
import np.com.manishtuladhar.waterreminderapp.utilities.PreferenceUtilities;

public class ReminderTasks {

    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";
    public static final String ACTION_DISMISS_NOTIFICATION= "dismiss-notification";
    public static final String ACTION_CHARGING_REMINDER= "charging-reminder";

    public static void executeTask(Context context,String action){
        if(ACTION_INCREMENT_WATER_COUNT.equals(action))
        {
            //water count increase
            incrementWaterCount(context);
        }
        else if(ACTION_DISMISS_NOTIFICATION.equals(action))
        {
            //clear notification
            NotificationUtils.clearAllNotification(context);
        }
        else if(ACTION_CHARGING_REMINDER.equals(action))
        {
            //charging state
            issueChargingReminder(context);
        }
    }

    private static void incrementWaterCount(Context context)
    {
        PreferenceUtilities.incrementWaterCount(context);
    }

    private static void issueChargingReminder(Context context)
    {
        PreferenceUtilities.incrementChargingReminderCount(context);
        NotificationUtils.remindUserWhenCharging(context);
    }
}

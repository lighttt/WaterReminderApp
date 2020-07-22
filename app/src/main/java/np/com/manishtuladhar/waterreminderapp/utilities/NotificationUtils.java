package np.com.manishtuladhar.waterreminderapp.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import np.com.manishtuladhar.waterreminderapp.MainActivity;
import np.com.manishtuladhar.waterreminderapp.R;
import np.com.manishtuladhar.waterreminderapp.sync.ReminderTasks;
import np.com.manishtuladhar.waterreminderapp.sync.WaterReminderIntentService;

public class NotificationUtils {

    //notification id
    private static final int WATER_REMINDER_NOTIFICATION_ID = 321;

    //pending intent
    private static final int WATER_REMINDER_PENDING_INTENT_ID = 123;

    //channel
    private static final String WATER_REMINDER_CHANNEL_ID = "reminder_notification";

    //action
    private static final int ACTION_DRINK_PENDING_INTENT_ID = 1;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;

    /**
     * Its helps us to go to mainactivity from notification manager using pendingIntent
     */
    private static PendingIntent contentIntent(Context context)
    {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context,
                WATER_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


    // ========================= NOTIFICATION =====================================

    public static void remindUserWhenCharging(Context context)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //oreo bhanda mathi ko devices ho bhane you need notification channel
        // haina bhane you dont need it
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(
                    WATER_REMINDER_CHANNEL_ID,
                    "Primary",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,WATER_REMINDER_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setLargeIcon(largeIcon(context))
                .setSmallIcon(R.drawable.ic_drink_notification)
                .setContentTitle(context.getString(R.string.notif_title))
                .setContentText(context.getString(R.string.notif_text))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.notif_text)
                ))
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent(context))
                //actions
                .addAction(drinkWaterAction(context))
                .addAction(ignoreReminderAction(context))
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID,notificationBuilder.build());


    }

    /**
     * Creating bitmap for large notification icon
     */
    private static Bitmap largeIcon(Context context)
    {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_baseline_local_drink_24);
        return largeIcon;
    }


    // ========================= NOTIFICATION Actions =====================================

    /**
     * Increase water count using action
     */
    private static NotificationCompat.Action drinkWaterAction(Context context)
    {
        //intent for increasing water count
        Intent incrementWaterCount = new Intent(context, WaterReminderIntentService.class);
        incrementWaterCount.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);

        PendingIntent incrementPendingIntent = PendingIntent.getService(context,
                ACTION_DRINK_PENDING_INTENT_ID,
                incrementWaterCount,
                PendingIntent.FLAG_CANCEL_CURRENT);

        //create action
        NotificationCompat.Action drinkWaterAction = new NotificationCompat.Action(R.drawable.ic_baseline_local_drink_24,
                "I did it!",
                incrementPendingIntent);
        return drinkWaterAction;

    }

    /**
     * Ignore notification action and clear notification
     */
    private static NotificationCompat.Action ignoreReminderAction(Context context)
    {
        //intent for increasing water count
        Intent ignoreReminderIntent = new Intent(context, WaterReminderIntentService.class);
        ignoreReminderIntent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);

        PendingIntent ignorePendingIntent = PendingIntent.getService(context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //create action
        NotificationCompat.Action ignoreReminderAction = new NotificationCompat.Action(R.drawable.ic_baseline_cancel_24,
                "No, thank you!",
                ignorePendingIntent);
        return ignoreReminderAction;
    }

    /**
     * Helps to clear all the notification
     */
    public static void clearAllNotification(Context context)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

}

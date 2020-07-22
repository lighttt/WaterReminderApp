package np.com.manishtuladhar.waterreminderapp.sync;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class ReminderUtils {

    private static final String TAG = "ReminderUtils";

    //charging state or not
    // 15 mins update or notify
    private static final int REMINDER_INTERVAL_MINUTES = 1;
    private static final int REMINDER_INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES);
    private static final int SYNC_TIME_SECONDS = REMINDER_INTERVAL_SECONDS;

    //tag
    private static final String REMINDER_JOB_TAG = "hydration_reminder_tag";

    //bool to check if our job is initialized or not
    private static boolean sInitialized;

    synchronized public static void scheduleChargingReminder(@NonNull  final Context context)
    {
        if(sInitialized){
            return;
        }
        //initialize
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        //job create
        Job reminderJob = dispatcher.newJobBuilder()
                //first set our job service
                .setService(WaterReminderFirebaseJobService.class)
                //set unique tags to the job
                .setTag(REMINDER_JOB_TAG)
                //set constraint where the job should run
                .setConstraints(Constraint.DEVICE_CHARGING)
                //jobs run until the device is rebooted
                .setLifetime(Lifetime.FOREVER)
                //frequently job should run
                .setRecurring(true)
                //automatically trigger our job run at 15 mins interval
                .setTrigger(Trigger.executionWindow(
                               30,
                                30 + 30))
                //replace old job with new one
                .setReplaceCurrent(true)
                .build();

        Log.e(TAG, "scheduleChargingReminder: job is scheduled and dispatched" );
        //schedule the job
        dispatcher.schedule(reminderJob);

        sInitialized = true;
    }
}

package np.com.manishtuladhar.waterreminderapp.sync;

import android.content.Context;

import np.com.manishtuladhar.waterreminderapp.utilities.PreferenceUtilities;

public class ReminderTasks {

    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";

    public static void executeTask(Context context,String action){
        if(ACTION_INCREMENT_WATER_COUNT.equals(action))
        {
            //water count increase
            incrementWaterCount(context);
        }
    }

    private static void incrementWaterCount(Context context)
    {
        PreferenceUtilities.incrementWaterCount(context);
    }
}

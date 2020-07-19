package np.com.manishtuladhar.waterreminderapp.sync;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class WaterReminderIntentService extends IntentService {

    //default constructor
    public WaterReminderIntentService() {
        super("WaterReminderIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //get action from intent
        String action = intent.getAction();
        //pass action
        ReminderTasks.executeTask(this,action);
    }
}

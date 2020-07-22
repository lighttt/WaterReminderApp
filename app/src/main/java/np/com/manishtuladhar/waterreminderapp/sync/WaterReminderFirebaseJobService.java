package np.com.manishtuladhar.waterreminderapp.sync;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.firebase.jobdispatcher.JobService;

public class WaterReminderFirebaseJobService extends JobService {

    private AsyncTask mBackgroundTask;

    /**
     * Entry point to your job and you should only use it on async or background thread
     */
    @Override
    public boolean onStartJob(@NonNull final com.firebase.jobdispatcher.JobParameters jobParameters) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                //send action using context as this service
                Context context = WaterReminderFirebaseJobService.this;
                ReminderTasks.executeTask(context,ReminderTasks.ACTION_CHARGING_REMINDER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(jobParameters,false);
            }
        };
        mBackgroundTask.execute();
        return true;
    }

    /**
     * Called when the schedule job is interrupted or execution is stopped due to some run time errors
     */
    @Override
    public boolean onStopJob(@NonNull com.firebase.jobdispatcher.JobParameters job) {
        //cancel out any background tasks so that if there is interrupt it can be addressed
        if(mBackgroundTask!=null)
        {
            mBackgroundTask.cancel(true);
        }
        return true;
    }
}

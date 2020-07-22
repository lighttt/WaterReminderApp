package np.com.manishtuladhar.waterreminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import np.com.manishtuladhar.waterreminderapp.sync.ReminderTasks;
import np.com.manishtuladhar.waterreminderapp.sync.ReminderUtils;
import np.com.manishtuladhar.waterreminderapp.sync.WaterReminderIntentService;
import np.com.manishtuladhar.waterreminderapp.utilities.NotificationUtils;
import np.com.manishtuladhar.waterreminderapp.utilities.PreferenceUtilities;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView mWaterCount,mChargingCount;
    private ImageView mChargingIV;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //views
        mWaterCount = findViewById(R.id.tv_water_count);
        mChargingCount = findViewById(R.id.tv_charging_remainder_count);
        mChargingIV = findViewById(R.id.iv_power_increment);

        //update view
        updateWaterCount();
        updateChargingReminderCount();

        //run the charging reminder
        ReminderUtils.scheduleChargingReminder(this);

        //setup shared prefs
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }


    /**
     * Update the water count into textview
     */
    private void updateWaterCount()
    {
        int waterCount = PreferenceUtilities.getWaterCount(this);
        mWaterCount.setText(waterCount + "");
    }

    /**
     * Update the charging reminder count into textview
     */
    private void updateChargingReminderCount()
    {
        int chargingReminder= PreferenceUtilities.getChargingReminder(this);
        String formattedString = getResources().getQuantityString(R.plurals.charge_notification_count,chargingReminder,chargingReminder);
        mChargingCount.setText(formattedString);
    }

    /**
     * Add water count by 1
     */
    public void incrementWater(View view) {
        //toast if increment
        if(mToast !=null) mToast.cancel();
        mToast = Toast.makeText(this,getString(R.string.drinking_water),Toast.LENGTH_SHORT);
        mToast.show();

        //increment and start service
        Intent incrementWaterCountIntent = new Intent(this, WaterReminderIntentService.class);
        incrementWaterCountIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
        startService(incrementWaterCountIntent);
    }

    //while prefs get changed
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(PreferenceUtilities.KEY_WATER_COUNT.equals(key))
        {
            updateWaterCount();
        }
        else if(PreferenceUtilities.KEY_CHARGING_REMINDER_COUNT.equals(key))
        {
            updateChargingReminderCount();
        }
    }

    //unregister the prefs
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //clean
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
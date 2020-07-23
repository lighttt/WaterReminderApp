package np.com.manishtuladhar.waterreminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
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

    ChargingBroadCastReceiver mChargingBroadCastReceiver;
    IntentFilter mChargingIntentFilter;

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

        //broadcast receiver
        mChargingBroadCastReceiver = new ChargingBroadCastReceiver();
        mChargingIntentFilter = new IntentFilter();

        //add filters to intent filter
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
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


    // =============================== BROADCAST RECEIVER =========================================

    private class ChargingBroadCastReceiver extends BroadcastReceiver {

        //we receive certain intent if the phone is charging or on any other state.
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean isCharging = action.equals(Intent.ACTION_POWER_CONNECTED);
            showCharging(isCharging);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //sticky broadcast
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            //use battery manager to know if there is changing in charging
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            showCharging(batteryManager.isCharging());
        }
        else{
            //using intent filter and battery manager to get the change in battery status
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent currentBatteryStatusIntent = registerReceiver(null,intentFilter);
            int batteryStatus = currentBatteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
            boolean isCharging = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING || batteryStatus == BatteryManager.BATTERY_STATUS_DISCHARGING;
            showCharging(isCharging);
        }
        registerReceiver(mChargingBroadCastReceiver,mChargingIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mChargingBroadCastReceiver);
    }

    /**
     * Change the charging color of the plug
     */
    private void showCharging(boolean isCharging)
    {
        if(isCharging)
        {
            mChargingIV.setImageResource(R.drawable.ic_baseline_green_power_24);
        }
        else{
            mChargingIV.setImageResource(R.drawable.ic_baseline_power_24);
        }
    }

}
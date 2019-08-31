package com.example.movietime.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Toast;

import com.example.movietime.R;
import com.example.movietime.notification.NotificationAlarmSetting;
import com.example.movietime.preference.SettingPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.movietime.notification.NotificationAlarmSetting.NOTIFICATION_DAILY;
import static com.example.movietime.notification.NotificationAlarmSetting.NOTIFICATION_RELEASE;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.sw_daily)
    SwitchCompat swDaily;
    @BindView(R.id.sw_release)
    SwitchCompat swRelease;

    private SettingPreference settingPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        final NotificationAlarmSetting notificationAlarmSetting = new NotificationAlarmSetting();

        settingPreference = new SettingPreference(this);
        swDaily.setChecked(settingPreference.getDaily());
        swRelease.setChecked(settingPreference.getRelease());
        swDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingPreference.setNotifDaily(swDaily.isChecked());
                if (swDaily.isChecked()){
                    notificationAlarmSetting.startNotification(NotificationActivity.this,NOTIFICATION_DAILY);
                    Toast.makeText(NotificationActivity.this, getApplicationContext().getString(R.string.notif_daily_setup,"on"), Toast.LENGTH_SHORT).show();
                }else {
                    notificationAlarmSetting.stopNotification(NotificationActivity.this, NOTIFICATION_DAILY );
                    Toast.makeText(NotificationActivity.this, getApplicationContext().getString(R.string.notif_daily_setup,"off"), Toast.LENGTH_SHORT).show();
                }
            }
        });

        swRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingPreference.setNotifRelease(swRelease.isChecked());
                if (swRelease.isChecked()){
                    notificationAlarmSetting.startNotification(NotificationActivity.this,NOTIFICATION_RELEASE);
                    Toast.makeText(NotificationActivity.this, getApplicationContext().getString(R.string.notif_release_setup,"on"), Toast.LENGTH_SHORT).show();
                }else {
                    notificationAlarmSetting.stopNotification(NotificationActivity.this, NOTIFICATION_RELEASE );
                    Toast.makeText(NotificationActivity.this, getApplicationContext().getString(R.string.notif_release_setup,"off"), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

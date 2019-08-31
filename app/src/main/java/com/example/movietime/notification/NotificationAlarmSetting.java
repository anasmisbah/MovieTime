package com.example.movietime.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.movietime.BuildConfig;
import com.example.movietime.R;
import com.example.movietime.activity.DetailActivity;
import com.example.movietime.activity.MainActivity;
import com.example.movietime.model.Movie;
import com.example.movietime.model.MovieResponse;
import com.example.movietime.service.ApiClient;
import com.example.movietime.service.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAlarmSetting extends BroadcastReceiver {

    private Context context;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private ApiInterface apiInterface;

    public final int ID_REQEUST_NOTIFDAILY = 10;
    public final int ID_REQEUST_NOTIFRELEASE = 20;

    public static final String ID_CHANNEL_RELEASE = "CHANNEL_01";
    public static final CharSequence NAME_CHANNEL_RELEASE = "CHANNEL_RELEASE";
    public static final String ID_CHANNEL_DAILY = "CHANNEL_02";
    public static final CharSequence NAME_CHANNEL_DAILY = "CHANNEL_DAILY";

    public static final String TYPE_NOTIFICATION = "TYPE_NOTIFICATION";
    public static final String NOTIFICATION_DAILY = "NOTIFICATION_DAILY";
    public static final String NOTIFICATION_RELEASE = "NOTIFICATION_RELEASE";

    public NotificationAlarmSetting() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String notif = intent.getStringExtra(TYPE_NOTIFICATION);
        if (notif.equalsIgnoreCase(NOTIFICATION_DAILY)){
            useDailyNotification();
        }else {
            useReleaseTodayNotification();
        }
    }

    private void useReleaseTodayNotification() {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final String formatTodayDate = simpleDateFormat.format(todayDate);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiInterface.getUpcoming(BuildConfig.api_key);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {


                for (Movie movie: response.body().getMovies()
                     ) {
                    if (movie.getReleaseDate().equalsIgnoreCase(formatTodayDate)){
                        movieArrayList.add(movie);
                    }
                }

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ID_CHANNEL_RELEASE);
                PendingIntent pendingIntent = null;

                if (movieArrayList.size() > 0){
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    int i =0;
                    for (Movie movie: movieArrayList
                         ) {

                        String text = context.getString(R.string.notif_release_content_text_movie,movie.getTitle());
                        intent.putExtra(DetailActivity.EXTRA_MOVIE,movie);
                        pendingIntent = TaskStackBuilder.create(context).addNextIntent(intent).getPendingIntent(i,PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentTitle(context.getString(R.string.notif_release_content_title))
                                .setContentText(text)
                                .setSmallIcon(R.drawable.ic_notifications_black)
                                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.image_notifications_black_48dp))
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);
                        if (notificationManager != null){
                            notificationManager.notify(i,builder.build());
                        }
                        i++;
                    }
                }else {
                 Intent intent = new Intent(context,MainActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                 pendingIntent = TaskStackBuilder.create(context).addNextIntent(intent).getPendingIntent(ID_REQEUST_NOTIFRELEASE,PendingIntent.FLAG_UPDATE_CURRENT);
                 builder.setContentTitle(context.getString(R.string.notif_release_content_title))
                         .setContentText(context.getString(R.string.notif_release_content_text))
                         .setSmallIcon(R.drawable.ic_notifications_black)
                         .setContentIntent(pendingIntent)
                         .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.image_notifications_black_48dp))
                         .setAutoCancel(true);
                    if (notificationManager != null){
                        notificationManager.notify(ID_REQEUST_NOTIFRELEASE,builder.build());
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    NotificationChannel channel = new NotificationChannel(ID_CHANNEL_RELEASE,NAME_CHANNEL_RELEASE,NotificationManager.IMPORTANCE_DEFAULT);
                    builder.setChannelId(ID_CHANNEL_RELEASE);
                    if (notificationManager != null){
                        notificationManager.createNotificationChannel(channel);
                    }
                }


            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });


    }

    private void useDailyNotification() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = TaskStackBuilder.create(context).addNextIntent(intent).getPendingIntent(ID_REQEUST_NOTIFDAILY,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,ID_CHANNEL_DAILY)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notifications_black)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.image_notifications_black_48dp))
                .setContentTitle("Daily Notification")
                .setContentText("Movie time is always missing you")
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(ID_CHANNEL_DAILY,NAME_CHANNEL_DAILY,NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(ID_CHANNEL_DAILY);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null){
            notificationManager.notify(ID_REQEUST_NOTIFDAILY, builder.build());
        }

    }

    public void startNotification(Context context,String type){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,NotificationAlarmSetting.class);
        intent.putExtra(TYPE_NOTIFICATION,type);
        Calendar calendar = Calendar.getInstance();
        PendingIntent pendingIntent = null;
        if (type.equalsIgnoreCase(NOTIFICATION_DAILY)){
            calendar.set(Calendar.HOUR_OF_DAY,7);
            calendar.set(Calendar.MINUTE,00);
            calendar.set(Calendar.SECOND,0);
            pendingIntent = PendingIntent.getBroadcast(context,ID_REQEUST_NOTIFDAILY,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        }else if (type.equalsIgnoreCase(NOTIFICATION_RELEASE)){
            calendar.set(Calendar.HOUR_OF_DAY,8);
            calendar.set(Calendar.MINUTE,00);
            calendar.set(Calendar.SECOND,0);
            pendingIntent = PendingIntent.getBroadcast(context,ID_REQEUST_NOTIFRELEASE,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        }
        if (alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        }
    }

    public void stopNotification(Context context,String type){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,NotificationAlarmSetting.class);
        PendingIntent pendingIntent = null;
        if (type.equalsIgnoreCase(NOTIFICATION_DAILY)){
            pendingIntent = PendingIntent.getBroadcast(context,ID_REQEUST_NOTIFDAILY,intent,0);
        }else if (type.equalsIgnoreCase(NOTIFICATION_RELEASE)){
            pendingIntent = PendingIntent.getBroadcast(context,ID_REQEUST_NOTIFRELEASE,intent,0);
        }
        if (alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }
    }
}

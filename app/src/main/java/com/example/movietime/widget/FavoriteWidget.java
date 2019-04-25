package com.example.movietime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.movietime.R;
import com.example.movietime.activity.DetailActivity;
import com.example.movietime.model.Movie;

import static com.example.movietime.activity.DetailActivity.EXTRA_MOVIE;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteWidget extends AppWidgetProvider {

    public static final String FAVORITE_ID = "com.example.movietime.widget.FAVORITE_ID";
    public static final String FAVORITE_TITLE = "com.example.movietime.widget.FAVORITE_TITLE";
    public static final String FAVORITE_NAME = "com.example.movietime.widget.FAVORITE_NAME";
    public static final String FAVORITE_VOTEAVERAGE = "com.example.movietime.widget.FAVORITE_VOTEAVERAGE";
    public static final String GOAPP = "com.example.movietime.widget.GOAPP";
    public static final String UPDATE_ACTION = "com.example.movietime.widget.UPDATE_ACTION";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, MovieWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_widget);
        views.setEmptyView(R.id.stackView,R.id.empty_view);
        views.setRemoteAdapter(R.id.stackView,intent);

        Intent goApp = new Intent(context,FavoriteWidget.class);
        goApp.setAction(GOAPP);
        goApp.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,goApp,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stackView,pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(GOAPP)){
            Intent intentGoAppMovie = new Intent(context,DetailActivity.class);
            Movie movie = new Movie();
            movie.setId(intent.getIntExtra(FAVORITE_ID,0));
            if (intent.getStringExtra(FAVORITE_TITLE) != null){
                movie.setTitle(intent.getStringExtra(FAVORITE_TITLE));
            }else if (intent.getStringExtra(FAVORITE_NAME) != null){
                movie.setName(intent.getStringExtra(FAVORITE_NAME));
            }
            movie.setVoteAverage(intent.getDoubleExtra(FAVORITE_VOTEAVERAGE,0));
            intentGoAppMovie.putExtra(EXTRA_MOVIE,movie);
            intentGoAppMovie.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intentGoAppMovie);
        }

        if (intent.getAction().equals(UPDATE_ACTION)){
            final ComponentName name = new ComponentName(context,FavoriteWidget.class);
            widgetManager.notifyAppWidgetViewDataChanged(widgetManager.getAppWidgetIds(name),R.id.stackView);
        }



        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


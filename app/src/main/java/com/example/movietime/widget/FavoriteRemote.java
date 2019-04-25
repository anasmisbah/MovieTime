package com.example.movietime.widget;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movietime.R;
import com.example.movietime.database.MovieDatabase;
import com.example.movietime.model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FavoriteRemote implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private List<Movie> movieArrayList = new ArrayList<>();
    private MovieDatabase movieDatabase;

    public FavoriteRemote(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        final long tokengenerator = Binder.clearCallingIdentity();
        movieArrayList.clear();
        movieDatabase = Room.databaseBuilder(context,MovieDatabase.class,"FavoriteDB").allowMainThreadQueries().build();
        movieDatabase.beginTransaction();
        movieArrayList.addAll(movieDatabase.getMovieDAO().getAllFavorites());
        movieDatabase.endTransaction();
        movieDatabase.close();
        Binder.restoreCallingIdentity(tokengenerator);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return movieArrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_widget);
        Movie movie = movieArrayList.get(position);
        String path = "https://image.tmdb.org/t/p/w154/"+movie.getPoster();
        try {
            Bitmap poster = Glide.with(context)
                    .asBitmap()
                    .load(path)
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
            remoteViews.setImageViewBitmap(R.id.img_widget,poster);
            if (movie.getTitle() != null){
                remoteViews.setTextViewText(R.id.tv_title_widget,movie.getTitle());
            }else if (movie.getName() != null){
                remoteViews.setTextViewText(R.id.tv_title_widget,movie.getName());
            }

        }catch (InterruptedException i){
            i.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putInt(FavoriteWidget.FAVORITE_ID,movie.getId());
        if (movie.getTitle() != null){
            bundle.putString(FavoriteWidget.FAVORITE_TITLE,movie.getTitle());
        }else if (movie.getName() != null){
            bundle.putString(FavoriteWidget.FAVORITE_NAME,movie.getName());
        }
        bundle.putDouble(FavoriteWidget.FAVORITE_VOTEAVERAGE,movie.getVoteAverage());
        Intent intentFilled = new Intent();
        intentFilled.putExtras(bundle);
        remoteViews.setOnClickFillInIntent(R.id.img_widget,intentFilled);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


}

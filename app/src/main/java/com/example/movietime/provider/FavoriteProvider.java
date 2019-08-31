package com.example.movietime.provider;

import android.arch.persistence.room.Room;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.movietime.database.MovieDatabase;
import com.example.movietime.model.Movie;

public class FavoriteProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.movietime";
    private static final String SCHEME = "content";
    public static String TABLE_FAVORITE = "favorite";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_FAVORITE)
            .build();

    private static final int FAVORITE_ITEM = 1;
    private static final int FAVORITE_ITEM_ID = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MovieDatabase movieDatabase;

    static {
        uriMatcher.addURI(AUTHORITY,TABLE_FAVORITE,FAVORITE_ITEM);
        uriMatcher.addURI(AUTHORITY,TABLE_FAVORITE+ "/#",FAVORITE_ITEM_ID);

    }
    @Override
    public boolean onCreate() {
        movieDatabase = Room.databaseBuilder(getContext(),MovieDatabase.class,"FavoriteDB").build();
        return true;
    }


    @Nullable
    @Override
    public Cursor query( @NonNull Uri uri,  @Nullable String[] projection,  @Nullable String selection,  @Nullable String[] selectionArgs,  @Nullable String sortOrder) {
        Cursor cursor;



        switch (uriMatcher.match(uri)){
            case FAVORITE_ITEM:
                cursor = movieDatabase.getMovieDAO().getContentFavorites();
                break;
            case FAVORITE_ITEM_ID:
                int id = Integer.parseInt(uri.getLastPathSegment());
                cursor = movieDatabase.getMovieDAO().getContentFavorite(id);
                break;
             default:
                 cursor = null;
                 break;
        }

        return cursor;
    }


    @Nullable
    @Override
    public String getType( @NonNull Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long added;
        switch (uriMatcher.match(uri)){
            case FAVORITE_ITEM:
                Log.d("kesini", "insert: ");
                added = movieDatabase.getMovieDAO().addFavorite(Movie.fromContentValues(values));
                break;
            default:
                added = 0;
        }
        return Uri.parse(CONTENT_URI+"/"+added);
    }

    @Override
    public int delete( @NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;

        switch (uriMatcher.match(uri)){
            case FAVORITE_ITEM_ID:
                deleted = movieDatabase.getMovieDAO().deleteFavoriteContent(Integer.parseInt(uri.getLastPathSegment()));
                break;
            default:
                deleted = 0;
        }
        return deleted;
    }

    @Override
    public int update( @NonNull Uri uri,  @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

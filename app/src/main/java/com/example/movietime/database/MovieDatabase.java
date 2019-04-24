package com.example.movietime.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.movietime.model.Movie;

@Database(entities = {Movie.class},version = 1,exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao getMovieDAO();
}

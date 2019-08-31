package com.example.movietime.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.example.movietime.model.Movie;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MovieDao {

    @Insert
    public long addFavorite(Movie movie);

    @Delete
    public int deleteFavorite(Movie movie);

    @Query("SELECT * FROM favorite WHERE category ==:category")
    public List<Movie> getFavorites(String category);

    @Query("SELECT * FROM favorite WHERE id ==:favoriteId")
    public Movie getFavorite(int favoriteId);

    @Query("SELECT * FROM favorite")
    public List<Movie> getAllFavorites();

    @Query("SELECT * FROM favorite")
    public Cursor getContentFavorites();

    @Query("SELECT * FROM favorite WHERE id ==:favoriteId")
    public Cursor getContentFavorite(int favoriteId);

    @Query("DELETE FROM favorite WHERE id ==:id")
    public int deleteFavoriteContent(int id);

}

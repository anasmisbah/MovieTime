package com.example.movietime.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity(tableName = "favorite")
public class Movie implements Parcelable {

    @ColumnInfo(name = "id")
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    private String title;

    @Ignore
    @SerializedName("overview")
    private String overview;

    @Ignore
    @SerializedName("original_language")
    private String language;

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    private double voteAverage;

    @Ignore
    @SerializedName("release_date")
    private String releaseDate;

    @Ignore
    @SerializedName("first_air_date")
    private String firstAirDate;

    @Ignore
    @SerializedName("genres")
    private ArrayList<Genre> genres;

    @Ignore
    @SerializedName("runtime")
    private int runtime;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    private String poster;

    @Ignore
    @SerializedName("backdrop_path")
    private String backdrop;

    @ColumnInfo(name = "category")
    private String category;

    @Ignore
    private Boolean favorite = false;

    protected Movie(Parcel in) {
        id = in.readInt();
        name = in.readString();
        title = in.readString();
        overview = in.readString();
        language = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
        firstAirDate = in.readString();
        runtime = in.readInt();
        poster = in.readString();
        backdrop = in.readString();
        category = in.readString();
        byte tmpFavorite = in.readByte();
        favorite = tmpFavorite == 0 ? null : tmpFavorite == 1;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    @Ignore
    public Movie() {
    }

    public Movie(int id, String name, String title, double voteAverage, String poster,  String category) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.overview = overview;
        this.language = language;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.firstAirDate = firstAirDate;
        this.genres = genres;
        this.runtime = runtime;
        this.poster = poster;
        this.backdrop = backdrop;
        this.category = category;
        this.favorite = favorite;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(language);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
        dest.writeString(firstAirDate);
        dest.writeInt(runtime);
        dest.writeString(poster);
        dest.writeString(backdrop);
        dest.writeString(category);
        dest.writeByte((byte) (favorite == null ? 0 : favorite ? 1 : 2));
    }
}

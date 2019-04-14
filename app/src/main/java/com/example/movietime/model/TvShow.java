package com.example.movietime.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TvShow implements Parcelable {
    private String title;
    private String overview;
    private String language;
    private String voteAverage;
    private String releaseDate;
    private int poster;

    public TvShow() {
    }

    public TvShow(String title, String voteAverage, String language, String overview, String releaseDate, int poster) {
        this.title = title;
        this.overview = overview;
        this.language = language;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.poster = poster;
    }

    protected TvShow(Parcel in) {
        title = in.readString();
        overview = in.readString();
        language = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
        poster = in.readInt();
    }

    public static final Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel in) {
            return new TvShow(in);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };

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

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getPoster() {
        return poster;
    }

    public void setPoster(int poster) {
        this.poster = poster;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(language);
        dest.writeString(voteAverage);
        dest.writeString(releaseDate);
        dest.writeInt(poster);
    }
}

package com.example.movietime.service;

import com.example.movietime.model.Movie;
import com.example.movietime.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/now_playing")
    Call<MovieResponse> getMovies(@Query("api_key") String apikey);

    @GET("{detail}/{id}")
    Call<Movie> getDetails(@Path("detail") String detail, @Path("id") int id, @Query("api_key") String apikey);

    @GET("tv/airing_today")
    Call<MovieResponse> getTvShows(@Query("api_key") String apikey);
}

package com.example.movietime.activity;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.movietime.BuildConfig;
import com.example.movietime.R;
import com.example.movietime.database.MovieDatabase;
import com.example.movietime.model.Genre;
import com.example.movietime.model.Movie;
import com.example.movietime.service.ApiClient;
import com.example.movietime.service.ApiInterface;
import com.example.movietime.widget.FavoriteWidget;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.movietime.widget.FavoriteWidget.UPDATE_ACTION;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";

    @BindView(R.id.detail_title)
    TextView textViewTitle;
    @BindView(R.id.detail_tglrilis)
    TextView textViewTglRilis;
    @BindView(R.id.detail_voteaverage)
    TextView textViewVote;
    @BindView(R.id.detail_overview)
    TextView textViewOverview;
    @BindView(R.id.imgCircle)
    CircleImageView imageView;
    @BindView(R.id.detail_language)
    TextView textViewLanguage;
    @BindView(R.id.detail_backdrop)
    ImageView backdrop;
    @BindView(R.id.detail_progress)
    ProgressBar progressBar;
    @BindView(R.id.detail_genres)
    TextView textViewDetailGenres;

    private ApiInterface apiInterface;
    private Movie movieApi;
    private String detailCurrent;
    private final String DETAIL_SAVE = "DETAIL_SAVE";
    private Menu menu;
    private MovieDatabase movieDatabase;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);


        movieDatabase = Room.databaseBuilder(getApplicationContext(), MovieDatabase.class, "FavoriteDB").allowMainThreadQueries().build();
        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        progressBar.setVisibility(View.VISIBLE);
        if (movie.getTitle() != null) {
            textViewTitle.setText(movie.getTitle());
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(movie.getTitle());
            }
            detailCurrent = "movie";

        } else if (movie.getName() != null) {
            textViewTitle.setText(movie.getName());
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(movie.getName());
            }
            detailCurrent = "tv";
        }

        movie.setCategory(detailCurrent);
        String vote_average = new DecimalFormat("##.##").format(movie.getVoteAverage());
        textViewVote.setText(vote_average);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (savedInstanceState != null) {
            movieApi = savedInstanceState.getParcelable(DETAIL_SAVE);
            Picasso.get().load("https://image.tmdb.org/t/p/w154/" + movieApi.getPoster()).into(imageView);
            Picasso.get().load("https://image.tmdb.org/t/p/w500/" + movieApi.getBackdrop()).into(backdrop);
            textViewLanguage.setText(movieApi.getLanguage());
            textViewOverview.setText(movieApi.getOverview());
            if (movie.getTitle() != null) {
                textViewTglRilis.setText(movieApi.getReleaseDate());

            } else if (movie.getName() != null) {
                textViewTglRilis.setText(movieApi.getFirstAirDate());
            }
            String detailgenre = "";
            ArrayList<Genre> genres = movieApi.getGenres();
            for (Genre genre : genres) {
                detailgenre = detailgenre + genre.getNamegenre() + " ";
            }
            textViewDetailGenres.setText(detailgenre);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            Call<Movie> call = apiInterface.getDetails(detailCurrent, movie.getId(), BuildConfig.api_key);
            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    movieApi = response.body();
                    Picasso.get().load("https://image.tmdb.org/t/p/w154/" + movieApi.getPoster()).into(imageView);
                    Picasso.get().load("https://image.tmdb.org/t/p/w500/" + movieApi.getBackdrop()).into(backdrop);
                    textViewLanguage.setText(movieApi.getLanguage());
                    textViewOverview.setText(movieApi.getOverview());
                    String detailgenre = "";
                    ArrayList<Genre> genres = movieApi.getGenres();
                    for (Genre genre : genres) {
                        detailgenre = detailgenre + genre.getNamegenre() + " ";
                    }
                    textViewDetailGenres.setText(detailgenre);
                    if (movie.getTitle() != null) {
                        textViewTglRilis.setText(movieApi.getReleaseDate());

                    } else if (movie.getName() != null) {
                        textViewTglRilis.setText(movieApi.getFirstAirDate());
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    new CheckFavorite().execute();
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {

                }
            });
        }


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(DETAIL_SAVE, movieApi);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.up_favorite:
                if (movie.getFavorite()) {
                    item.setIcon(R.drawable.ic_favorite_white_24dp);
                    movieDatabase.getMovieDAO().deleteFavorite(movie);
                    Snackbar.make(getWindow().getDecorView().getRootView(), getResources().getString(R.string.info_deleted), Snackbar.LENGTH_SHORT).show();
                } else {
                    item.setIcon(R.drawable.ic_favorite_black);
                    movieDatabase.getMovieDAO().addFavorite(movie);
                    Snackbar.make(getWindow().getDecorView().getRootView(), getResources().getString(R.string.info_added), Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
        Intent updateFavoriteWidget = new Intent(this, FavoriteWidget.class);
        updateFavoriteWidget.setAction(UPDATE_ACTION);
        sendBroadcast(updateFavoriteWidget);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private class CheckFavorite extends AsyncTask<Void, Void, Movie> {


        @Override
        protected Movie doInBackground(Void... voids) {
            Movie movieget = movieDatabase.getMovieDAO().getFavorite(movie.getId());
            return movieget;
        }

        @Override
        protected void onPostExecute(Movie moviePost) {
            super.onPostExecute(moviePost);
            if (moviePost != null) {
                menu.getItem(0).setIcon(R.drawable.ic_favorite_black);
                movie.setFavorite(true);
            }
        }
    }
}

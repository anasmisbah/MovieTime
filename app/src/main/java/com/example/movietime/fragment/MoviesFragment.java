package com.example.movietime.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.movietime.BuildConfig;
import com.example.movietime.R;
import com.example.movietime.adapter.MovieAdapter;
import com.example.movietime.model.Movie;
import com.example.movietime.model.MovieResponse;
import com.example.movietime.service.ApiClient;
import com.example.movietime.service.ApiInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {

    @BindView(R.id.movie_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.movie_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.et_search_movie)
    EditText etSearchMovie;
    @BindView(R.id.btn_search_movie)
    Button btnSearchMovie;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private ApiInterface apiInterface;
    private final String MOVIE_LIST = "MOVIE_LIST";

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        final MovieAdapter movieAdapter;
        movieAdapter = new MovieAdapter(getContext());

        if (savedInstanceState != null) {
            movieArrayList = (ArrayList<Movie>) savedInstanceState.getSerializable(MOVIE_LIST);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            movieAdapter.setMovieArrayList(movieArrayList);
            recyclerView.setAdapter(movieAdapter);
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            Call<MovieResponse> call = apiInterface.getMovies(BuildConfig.api_key);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    movieArrayList = response.body().getMovies();
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                    movieAdapter.setMovieArrayList(movieArrayList);
                    recyclerView.setAdapter(movieAdapter);
                    progressBar.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Log.e("ERROR", "onFailure: " + t);
                }
            });
        }


        btnSearchMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                String key = etSearchMovie.getText().toString();
                if (TextUtils.isEmpty(key)){
                    Call<MovieResponse> call = apiInterface.getMovies(BuildConfig.api_key);
                    call.enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            movieArrayList = response.body().getMovies();
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                            movieAdapter.setMovieArrayList(movieArrayList);
                            recyclerView.setAdapter(movieAdapter);
                            progressBar.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<MovieResponse> call, Throwable t) {
                            Log.e("ERROR", "onFailure: " + t);
                        }
                    });
                }else {
                    Call<MovieResponse> call = apiInterface.SearchItem("movie",BuildConfig.api_key,key);
                    call.enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            movieArrayList = response.body().getMovies();
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                            movieAdapter.setMovieArrayList(movieArrayList);
                            recyclerView.setAdapter(movieAdapter);
                            progressBar.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<MovieResponse> call, Throwable t) {

                        }
                    });
                }

            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(MOVIE_LIST, movieArrayList);
        super.onSaveInstanceState(outState);
    }


}

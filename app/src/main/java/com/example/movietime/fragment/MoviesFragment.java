package com.example.movietime.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(MOVIE_LIST, movieArrayList);
        super.onSaveInstanceState(outState);
    }
}

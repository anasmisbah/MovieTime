package com.example.movietime.fragment;


import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.movietime.R;
import com.example.movietime.adapter.MovieAdapter;
import com.example.movietime.database.MovieDatabase;
import com.example.movietime.model.Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFavFragment extends Fragment {


    @BindView(R.id.tvshowfav_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tvshowfav_recycler)
    RecyclerView recyclerView;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private MovieDatabase movieDatabase;
    private MovieAdapter movieAdapter;
    public TvShowFavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tv_show_fav, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        movieAdapter = new MovieAdapter(getContext());
        movieDatabase = Room.databaseBuilder(getContext(),MovieDatabase.class,"FavoriteDB").build();
        new GetAllFavoritesAsyncTask().execute();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        movieAdapter.setMovieArrayList(movieArrayList);
        recyclerView.setAdapter(movieAdapter);
    }

    private class GetAllFavoritesAsyncTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            movieArrayList.clear();
            movieArrayList.addAll(movieDatabase.getMovieDAO().getFavorites("tv"));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            movieAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        new GetAllFavoritesAsyncTask().execute();
        super.onResume();
    }
}

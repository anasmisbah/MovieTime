package com.example.movietime.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movietime.R;
import com.example.movietime.activity.DetailActivity;
import com.example.movietime.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CardViewHolder> {

    private Context context;
    private ArrayList<Movie> movieArrayList;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Movie> getMovieArrayList() {
        return movieArrayList;
    }

    public void setMovieArrayList(ArrayList<Movie> movieArrayList) {
        this.movieArrayList = movieArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_item, viewGroup, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder cardViewHolder, int i) {

        Movie movie = getMovieArrayList().get(i);

        cardViewHolder.cardView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale));
        if (movie.getTitle() != null) {
            cardViewHolder.textViewTitle.setText(movie.getTitle());
        } else if (movie.getName() != null) {
            cardViewHolder.textViewTitle.setText(movie.getName());
        }


        Picasso.get().load("https://image.tmdb.org/t/p/w154/" + movie.getPoster()).into(cardViewHolder.imageViewPoster);
        String vote_average = new DecimalFormat("##.##").format(movie.getVoteAverage());
        cardViewHolder.textViewVoteAverage.setText(vote_average);
        cardViewHolder.cardView.setOnClickListener(new CustomOnItemClickListener(i, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Movie movie1 = movieArrayList.get(position);
                Intent detailIntent = new Intent(context, DetailActivity.class);
                detailIntent.putExtra(DetailActivity.EXTRA_MOVIE, movie1);
                context.startActivity(detailIntent);
            }
        }));
    }

    @Override
    public int getItemCount() {
        if (movieArrayList == null) {
            return 0;
        }
        return movieArrayList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView textViewTitle;
        @BindView(R.id.tv_voteaverage)
        TextView textViewVoteAverage;
        @BindView(R.id.card_view_id)
        CardView cardView;
        @BindView(R.id.poster)
        ImageView imageViewPoster;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

package com.example.movies;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailActivity";

    private static final String EXTRA_MOVIE = "movie";

    private MovieDetailViewModel viewModel;

    private ImageView imageViewPoster;
    private TextView textViewTitle;
    private TextView textViewYear;
    private TextView textViewDescription;
    private ImageView imageViewLike;

    private RecyclerView recyclerViewTrailers;
    private TrailersAdapter trailersAdapter;

    private RecyclerView recyclerViewReviews;
    private ReviewsAdapter reviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_detail);

        viewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);

        init();
        trailersAdapter = new TrailersAdapter();
        recyclerViewTrailers.setAdapter(trailersAdapter);

        reviewsAdapter = new ReviewsAdapter();
        recyclerViewReviews.setAdapter(reviewsAdapter);


        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
        Glide.with(this)
                .load(movie.getPoster().getUrl())
                .into(imageViewPoster);
        textViewTitle.setText(movie.getName());
        textViewYear.setText(String.valueOf(movie.getYear()));
        textViewDescription.setText(movie.getDescription());

        viewModel.loadTrailers(movie.getId());
        viewModel.getTrailers().observe(this, trailers -> trailersAdapter.setTrailers(trailers));

        viewModel.loadReviews(movie.getId());
        viewModel.getReviews().observe(this, reviews -> reviewsAdapter.setReviews(reviews));

        trailersAdapter.setOnTrailerClickListener(trailer -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(trailer.getUrl()));
            startActivity(intent);
        });

        Drawable likeOff = ContextCompat.getDrawable(
                MovieDetailActivity.this,
                R.drawable.icon_before_add_to_favorite
        );

        Drawable likeOn = ContextCompat.getDrawable(
                MovieDetailActivity.this,
                R.drawable.icon_after_add_to_favorite
        );

        Drawable likeBackground = ContextCompat.getDrawable(
                MovieDetailActivity.this,
                R.drawable.circle_for_favorite
        );
        viewModel.getFavoriteMovie(movie.getId()).observe(this, movieFromDB -> {
             if(movieFromDB == null) {
                 imageViewLike.setImageDrawable(likeOff);
                 imageViewLike.setBackground(likeBackground);
                 imageViewLike.setOnClickListener(view -> viewModel.insertMovie(movie));
             } else {
                 imageViewLike.setImageDrawable(likeOn);
                 imageViewLike.setBackground(likeBackground);
                 imageViewLike.setOnClickListener(view -> viewModel.removeMovie(movie.getId()));
             }
        });
    }

    private void init() {
        imageViewPoster = findViewById(R.id.imageViewPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewYear = findViewById(R.id.textViewYear);
        textViewDescription = findViewById(R.id.textViewDescription);
        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        imageViewLike = findViewById(R.id.imageViewLike);
    }

    public static Intent newIntent(Context context,Movie movie){
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE,movie);
        return intent;
    }
}
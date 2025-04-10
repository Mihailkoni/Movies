package com.example.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteMovieActivity extends AppCompatActivity {

    private FrameLayout frameLayoutEmptyFavorites;
    private RecyclerView recyclerViewFavoriteMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite_movie);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        recyclerViewFavoriteMovies.setLayoutManager(new GridLayoutManager(this,2));
        MoviesAdapter moviesAdapter = new MoviesAdapter();
        recyclerViewFavoriteMovies.setAdapter(moviesAdapter);
        moviesAdapter.setOnMovieClickListener(movie -> {
            Intent intent = MovieDetailActivity.newIntent(
                    FavoriteMovieActivity.this,
                    movie
            );
            startActivity(intent);
        });
        FavoriteMovieViewModel favoriteMovieViewModel = new ViewModelProvider(this).get(
                FavoriteMovieViewModel.class
        );

        favoriteMovieViewModel.getFavoriteMovies().observe(this, movies -> {
            if(movies != null && !movies.isEmpty()) {
                moviesAdapter.setMovies(movies);
                showEmptyError();
            } else {
                moviesAdapter.setMovies(movies);
                hideEmptyError();
            }
        });
    }

    private void init() {
        frameLayoutEmptyFavorites = findViewById(R.id.frameLayoutEmptyFavorites);
        recyclerViewFavoriteMovies = findViewById(R.id.recyclerViewFavoriteMovies);
    }

    private void showEmptyError() {
        frameLayoutEmptyFavorites.setVisibility(View.GONE);
    }

    private void hideEmptyError() {
        frameLayoutEmptyFavorites.setVisibility(View.VISIBLE);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context,FavoriteMovieActivity.class);
    }
}
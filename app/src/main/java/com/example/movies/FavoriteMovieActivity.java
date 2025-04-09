package com.example.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    private ImageView imageViewEmptyFavorites;
    private TextView textViewEmptyFavorites;
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
            if(!movies.isEmpty()) {
                moviesAdapter.setMovies(movies);
                imageViewEmptyFavorites.setVisibility(View.GONE);
                textViewEmptyFavorites.setVisibility(View.GONE);
            } else {
                imageViewEmptyFavorites.setVisibility(View.VISIBLE);
                textViewEmptyFavorites.setVisibility(View.VISIBLE);
            }
        });
    }

    private void init() {
        imageViewEmptyFavorites = findViewById(R.id.imageViewEmptyFavorites);
        textViewEmptyFavorites = findViewById(R.id.textViewEmptyFavorites);
        recyclerViewFavoriteMovies = findViewById(R.id.recyclerViewFavoriteMovies);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context,FavoriteMovieActivity.class);
    }
}
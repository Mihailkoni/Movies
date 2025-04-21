package com.example.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    /**
     * Добавить кнопку "смотреть", и если в api нет просмотра на кинопоиске, не отображать её.
     * Исправить баг при отсоединении интернета когда можно при быстром нажатии зайти в описание фильма.
     * На экране фильма и скрыть трейлеры, отзывы, чтобы их можно было открывать при нажатии.
     */
    private RecyclerView recyclerViewMovies;
    private ProgressBar progressBarLoading;
    private FrameLayout frameLayoutError;
    private TextView textViewError;
    private Button buttonRetry;

    private MainViewModel viewModel;
    private MoviesAdapter moviesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        moviesAdapter = new MoviesAdapter();
        recyclerViewMovies.setAdapter(moviesAdapter);
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(this,2));

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, movies -> moviesAdapter.setMovies(movies));
        viewModel.getIsLoading().observe(this, isLoading -> {
            if(isLoading) {
                progressBarLoading.setVisibility(View.VISIBLE);
            } else {
                progressBarLoading.setVisibility(View.GONE);
            }
        });
        viewModel.getIsError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                showError(error);
            } else {
                hideError();
            }
        });
        buttonRetry.setOnClickListener(view -> {
            hideError();
            viewModel.loadMovies();
        });

        moviesAdapter.setOnReachEndListener(() -> viewModel.loadMovies());
        moviesAdapter.setOnMovieClickListener(movie -> {
            Intent intent = MovieDetailActivity.newIntent(MainActivity.this,movie);
            startActivity(intent);
        });
    }

    private void showError(String errorMessage) {
        String error = ContextCompat.getString(
                MainActivity.this,
                R.string.error_template
        ) + " " + errorMessage;
        textViewError.setText(error);
        frameLayoutError.setVisibility(View.VISIBLE);
        frameLayoutError.bringToFront();
    }

    private void hideError() {
        frameLayoutError.setVisibility(View.GONE);
    }

    private void init() {
        progressBarLoading = findViewById(R.id.progressBarLoading);
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        frameLayoutError = findViewById(R.id.frameLayoutError);
        textViewError = findViewById(R.id.textViewError);
        buttonRetry = findViewById(R.id.buttonRetry);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_movies_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.favoriteMoviesMenu) {
            Intent intent = FavoriteMovieActivity.newIntent(this);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
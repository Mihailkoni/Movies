package com.example.movies;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class FavoriteMovieViewModel extends AndroidViewModel {

    private static final String TAG = "FavoriteMovieViewModel";

    private final MovieDao movieDao;

    private final MutableLiveData<List<Movie>> favoriteMovies = new MutableLiveData<>();

    public FavoriteMovieViewModel(@NonNull Application application) {
        super(application);
        movieDao = MovieDatabase.getInstance(application).movieDao();
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return movieDao.getAllFavoriteMovies();
    }

}

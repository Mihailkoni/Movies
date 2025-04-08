package com.example.movies;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM favorite_movies")
    LiveData<List<Movie>> getAllFavoriteMovies();

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    LiveData<Movie> getFavoriteMovie(int movieId);

    @Insert
    Completable insertMovie(Movie movie);

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    Completable removeMovie(int movieId);
}

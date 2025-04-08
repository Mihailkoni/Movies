package com.example.movies;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailViewModel extends AndroidViewModel {

    private static final String TAG = "MovieDetailViewModel";

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<List<Trailer>> trailers = new MutableLiveData<>();
    private final MutableLiveData<List<Review>> reviews = new MutableLiveData<>();

    private final MovieDao movieDao;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        movieDao = MovieDatabase.getInstance(application).movieDao();
    }

    public LiveData<Movie> getFavoriteMovie(int movieId) {
        return movieDao.getFavoriteMovie(movieId);
    }

    public LiveData<List<Trailer>> getTrailers() {
        return trailers;
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    public void loadTrailers(int id) {
        Disposable disposable;
        disposable = ApiFactory.apiService.loadTrailers(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(trailerResponse -> trailerResponse.getTrailersList().getTrailers())
                .subscribe(trailers::setValue, throwable -> Log.d(TAG,throwable.toString()));
        compositeDisposable.add(disposable);
    }

    public void loadReviews(int id) {
        Disposable disposable;
        disposable = ApiFactory.apiService.loadReviews(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ReviewResponse::getReviews)
                .subscribe(reviews::setValue, throwable -> Log.d(TAG,throwable.toString()));
        compositeDisposable.add(disposable);
    }

    public void insertMovie(Movie movie) {
        Disposable disposable = movieDao.insertMovie(movie)
                .subscribeOn(Schedulers.io())
                .subscribe();
        compositeDisposable.add(disposable);
    }

    public void removeMovie(int movieId) {
        Disposable disposable = movieDao.removeMovie(movieId)
                .subscribeOn(Schedulers.io())
                .subscribe();
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}

package com.example.movies;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class MovieDetailViewModel extends AndroidViewModel {

    private static final String TAG = "MovieDetailViewModel";
    private final String INTERNET_ERROR = ContextCompat.getString(
            this.getApplication(),
            R.string.internet_error
    );
    private final String ACCESS_DENIED_ERROR = ContextCompat.getString(
            this.getApplication(),
            R.string.access_denied_error
    );
    private final String UNKNOWN_ERROR = ContextCompat.getString(
            this.getApplication(),
            R.string.unknown_error
    );
    private final String SERVER_ERROR = ContextCompat.getString(
            this.getApplication(),
            R.string.server_error
    );
    private final String TIMEOUT_ERROR = ContextCompat.getString(
            this.getApplication(),
            R.string.timeout_error
    );

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<List<Trailer>> trailers = new MutableLiveData<>();
    private final MutableLiveData<List<Review>> reviews = new MutableLiveData<>();
    private final MutableLiveData<String> isErrorReviews = new MutableLiveData<>();
    private final MutableLiveData<String> isErrorTrailers = new MutableLiveData<>();

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

    public MutableLiveData<String> getIsErrorReviews() {
        return isErrorReviews;
    }

    public MutableLiveData<String> getIsErrorTrailers() {
        return isErrorTrailers;
    }

    public void loadTrailers(int id) {
        Disposable disposable;
        disposable = ApiFactory.apiService.loadTrailers(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(trailerResponse -> trailerResponse.getTrailersList().getTrailers())
                .subscribe(trailers::setValue, throwable -> {
                    handleError(throwable,isErrorTrailers);
                    Log.d(TAG,throwable.toString());
                });
        compositeDisposable.add(disposable);
    }

    public void loadReviews(int id) {
        Disposable disposable;
        disposable = ApiFactory.apiService.loadReviews(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ReviewResponse::getReviews)
                .subscribe(reviews::setValue, throwable -> {
                    handleError(throwable,isErrorReviews);
                    Log.d(TAG,throwable.toString());
                });
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

    private void handleError(Throwable error,MutableLiveData<String> isError) {
        if (error instanceof UnknownHostException) {
            isError.setValue(INTERNET_ERROR);
        } else if (error instanceof HttpException) {
            HttpException httpException = (HttpException) error;
            if (httpException.code() == 401 || httpException.code() == 403) {
                isError.setValue(ACCESS_DENIED_ERROR);
            } else if (httpException.code() >= 500) {
                isError.setValue(SERVER_ERROR);
            } else {
                isError.setValue(UNKNOWN_ERROR);
            }
        } else if (error instanceof SocketTimeoutException) {
            isError.setValue(TIMEOUT_ERROR);
        } else {
            isError.setValue(UNKNOWN_ERROR);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}

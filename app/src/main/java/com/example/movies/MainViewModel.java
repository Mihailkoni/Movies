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
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";
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

    private final MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> isError = new MutableLiveData<>();

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private int page = 1;

    public MainViewModel(@NonNull Application application) {
        super(application);
        loadMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getIsError() {
        return isError;
    }

    public void loadMovies() {
        Boolean loading = isLoading.getValue();
        if(loading != null && loading){
            return;
        }
        Disposable disposable = ApiFactory.apiService.loadMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                .doAfterTerminate(() -> isLoading.setValue(false))
                .subscribe(
                        movieResponse -> {
                            List<Movie> loadedMovies = movies.getValue();
                            if (loadedMovies != null) {
                                loadedMovies.addAll(movieResponse.getMovies());
                                movies.setValue(loadedMovies);
                            } else {
                                movies.setValue(movieResponse.getMovies());
                            }
                            Log.d(TAG, "Loaded: " + page);
                            page++;
                        },
                        throwable -> {
                            handleError(throwable);
                            Log.d(TAG,throwable.toString());
                        }
                );
        compositeDisposable.add(disposable);
    }

    private void handleError(Throwable error) {
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

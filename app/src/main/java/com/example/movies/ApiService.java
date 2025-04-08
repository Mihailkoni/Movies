package com.example.movies;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    /** В запрос можно передавать различные параметры динамически
     * с помощью аннотации @Query ("Название параметра" сам параметр).
     */

    String TOKEN = "N52YBFW-PNDM406-JARR9FH-C5G0139";

    @GET("movie?rating.kp=7-10&sortField=votes.kp&sortType=-1&limit=50&type=anime")
    @Headers("X-API-KEY:" + TOKEN)
    Single<MovieResponse> loadMovies(@Query("page") int page);

    @GET("movie/{id}")
    @Headers("X-API-KEY:" + TOKEN)
    Single<TrailerResponse> loadTrailers(@Path("id") int id);

    @GET("review?page=1&limit=10&selectFields=type&selectFields=review&selectFields=author&sortField=date&sortType=-1")
    @Headers("X-API-KEY:" + TOKEN)
    Single<ReviewResponse> loadReviews(@Query("movieId") int id);
}

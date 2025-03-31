package com.example.movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {
    /**Т.к мы получаем из json массив с названием "docs", а у нас список
    называется "movies", из-за этого делаем аннотацию, чтобы было понятно куда загружать данные.*/
    @SerializedName("docs")
    private List<Movie> movies;

    public MovieResponse(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "movies=" + movies +
                '}';
    }
}

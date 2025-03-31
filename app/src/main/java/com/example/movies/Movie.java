package com.example.movies;

import com.google.gson.annotations.SerializedName;

public class Movie {
    /** Здесь мы делаем аннотации рядом с каждым полем т.к каждое приложение
    при загрузке в play market проходит <u><b>обфускации</u></b> и
    все названия (переменных, классов и т.д) меняются,
    а здесь мы будем загружать данные из интернета и Retrofit'у будет непонятно куда загружать данные.
     <b>Во всех классах, где используется интернет нужно делать такие аннотации.</b><br>
     Такие классы, которые мы создали (MovieResponse,Movie,Rating,Poster) называются POJO(Plain Old Java Object).
     Это класс у которого есть поля, конструктор, геттеры и сеттеры.
     Их можно сгенирировать на онлайн конструторах вставив json данные(Например https://www.jsonschema2pojo.org/).*/
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("year")
    private int year;

    @SerializedName("poster")
    private Poster poster;

    @SerializedName("rating")
    private Rating rating;

    public Movie(int id, String name, String description, int year, Poster poster, Rating rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.year = year;
        this.poster = poster;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getYear() {
        return year;
    }

    public Poster getPoster() {
        return poster;
    }

    public Rating getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", year=" + year +
                ", poster=" + poster +
                ", rating=" + rating +
                '}';
    }
}

package com.example.movies;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "favorite_movies")
public class Movie implements Serializable{
    /** Здесь мы делаем аннотации рядом с каждым полем т.к каждое приложение
    при загрузке в play market проходит <u><b>обфускации</u></b> и
    все названия (переменных, классов и т.д) меняются,
    а здесь мы будем загружать данные из интернета и Retrofit'у будет непонятно куда загружать данные.
     <b>Во всех классах, где используется интернет нужно делать такие аннотации.</b><br>
     Такие классы, которые мы создали (MovieResponse,Movie,Rating,Poster) называются POJO(Plain Old Java Object).
     Это класс у которого есть поля, конструктор, геттеры и сеттеры.
     Их можно сгенирировать на онлайн конструторах вставив json данные(Например https://www.jsonschema2pojo.org/).*/
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("year")
    private int year;

    /**
     Аннотация <b>@Ebbedded</b> даёт понять Room, что нужно встроить поля
     данного энкземпляра класса (в этом примере Rating или Poster)
     в объект в котором они объявлены (в этом примере Movie).
     То есть, все поля из энкземпляров классов poster и rating будут внедрены в объект movie.
     */
    @SerializedName("poster")
    @Embedded
    private Poster poster;

    @SerializedName("rating")
    @Embedded
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

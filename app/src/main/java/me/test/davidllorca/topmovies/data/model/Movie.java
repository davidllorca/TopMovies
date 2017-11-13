package me.test.davidllorca.topmovies.data.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Model of class
 */
public class Movie {

    @SerializedName("id")
    int id;
    @SerializedName("title")
    String title;
    @SerializedName("poster_path")
    String posterPath;
    @SerializedName("vote_average")
    String voteAverage;
    @SerializedName("overview")
    @Nullable String overview;

}

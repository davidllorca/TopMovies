package me.test.davidllorca.topmovies.data.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.test.davidllorca.topmovies.data.model.Movie;

/**
 * Created by David Llorca <davidllorcabaron@gmail.com> on 13/11/17.
 */

public class MovieListResponse {

    @SerializedName("page")
    int page;

    @SerializedName("results")
    List<Movie> movies;

    @SerializedName("total_pages")
    int totalPages;

    public int getPage() {
        return page;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public int getTotalPages() {
        return totalPages;
    }
}

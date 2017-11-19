package me.test.davidllorca.topmovies.data.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.test.davidllorca.topmovies.data.model.Movie;

/**
 * Response's model of movie list.
 */
public class MovieListResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Movie> movies;

    @SerializedName("total_pages")
    private int totalPages;

    int getPage() {
        return page;
    }

    List<Movie> getMovies() {
        return movies;
    }

    int getTotalPages() {
        return totalPages;
    }
}

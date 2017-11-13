package me.test.davidllorca.topmovies.data;

import java.util.List;

import io.reactivex.Single;
import me.test.davidllorca.topmovies.data.model.Movie;

/**
 * Contract between data source and {@link MoviesRepository}.
 */
public interface MoviesDataSource {

    Single<List<Movie>> getTopRated();

    Single<List<Movie>> getSimilar(String movieId);

}

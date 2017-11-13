package me.test.davidllorca.topmovies.data;

import java.util.List;

import io.reactivex.Flowable;
import me.test.davidllorca.topmovies.data.model.Movie;

/**
 * Contract between data source and {@link MoviesRepository}.
 */
public interface MoviesDataSource {

    Flowable<List<Movie>> getTopRated();

    Flowable<List<Movie>> getSimilar(String movieId);

}

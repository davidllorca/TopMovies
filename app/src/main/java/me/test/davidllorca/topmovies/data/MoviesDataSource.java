package me.test.davidllorca.topmovies.data;

import java.util.List;

import io.reactivex.Single;
import me.test.davidllorca.topmovies.data.model.Movie;

/**
 * Contract between data sources and {@link MoviesRepository}.
 */
public interface MoviesDataSource {

    Single<List<Movie>> getTopRated(int offset);

    Single<List<Movie>> getSimilar(int targetMovieId, int offset);

}

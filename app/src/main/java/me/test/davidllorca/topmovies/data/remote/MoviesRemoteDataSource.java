package me.test.davidllorca.topmovies.data.remote;

import java.util.List;

import io.reactivex.Single;
import me.test.davidllorca.topmovies.data.MoviesDataSource;
import me.test.davidllorca.topmovies.data.model.Movie;

/**
 * Implementation of remote data source.
 */
public class MoviesRemoteDataSource implements MoviesDataSource {

    MovieService mService = RetrofitHelper.createRetrofitService(MovieService.class);

    @Override
    public Single<List<Movie>> getTopRated() {
        return mService.getTopRated().map(response -> response.getMovies());
    }

    @Override
    public Single<List<Movie>> getSimilar(String movieId) {
        return mService.getSimilar(movieId).map(response -> response.getMovies());
    }

}

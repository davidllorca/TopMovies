package me.test.davidllorca.topmovies.data.remote;

import java.util.List;

import io.reactivex.Single;
import me.test.davidllorca.topmovies.data.MoviesDataSource;
import me.test.davidllorca.topmovies.data.model.Movie;

/**
 * Logic implementation of remote data source.
 */
public class MoviesRemoteDataSource implements MoviesDataSource {

    //Singleton instantiation
    private static final Object LOCK = new Object();
    private static MoviesRemoteDataSource sInstance;
    MovieService mService;

    private MoviesRemoteDataSource() {
        mService = RetrofitHelper.createRetrofitService(MovieService.class);
    }

    /**
     * Return single instance of remote data client.
     *
     * @return {@link MoviesRemoteDataSource} instance.
     */
    public static synchronized MoviesRemoteDataSource getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MoviesRemoteDataSource();
            }
        }
        return sInstance;
    }

    @Override
    public Single<List<Movie>> getTopRated(int offset) {
        return mService.getTopRated(offset).map(response -> response.getMovies());
    }

    @Override
    public Single<List<Movie>> getSimilar(int targetMovieId, int offset) {
        return mService.getSimilar(targetMovieId, offset).map(response -> response.getMovies());
    }

}

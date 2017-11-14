package me.test.davidllorca.topmovies.data.remote;

import java.util.List;

import io.reactivex.Single;
import me.test.davidllorca.topmovies.data.MoviesDataSource;
import me.test.davidllorca.topmovies.data.MoviesRepository;
import me.test.davidllorca.topmovies.data.model.Movie;

/**
 * Implementation of remote data source.
 */
public class MoviesRemoteDataSource implements MoviesDataSource {

    //Singleton instantiation
    private static final Object LOCK = new Object();
    private static MoviesRemoteDataSource sInstance;
    MovieService mService;
    private MoviesDataSource mRemoteDataSource;

    private MoviesRemoteDataSource() {
        mService = RetrofitHelper.createRetrofitService(MovieService.class);
    }

    /**
     * Return single instance of remote data client.
     *
     * @return {@link MoviesRepository} instance.
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
    public Single<List<Movie>> getTopRated() {
        return mService.getTopRated().map(response -> response.getMovies());
    }

    @Override
    public Single<List<Movie>> getSimilar(String movieId) {
        return mService.getSimilar(movieId).map(response -> response.getMovies());
    }

}

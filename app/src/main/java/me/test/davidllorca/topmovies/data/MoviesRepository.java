package me.test.davidllorca.topmovies.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Single;
import me.test.davidllorca.topmovies.data.model.Movie;

/**
 * Repository implementation of data layer.
 * <p>
 * (NOTE: Just remote source covered)
 */
public class MoviesRepository implements MoviesDataSource {

    //Singleton instantiation
    private static final Object LOCK = new Object();
    private static MoviesRepository sInstance;

    private MoviesDataSource mRemoteDataSource;

    private MoviesRepository(@Nullable MoviesDataSource localDataSource, @NonNull
            MoviesDataSource remoteDataSource) {
        //NOTE: local data feature is not implemented in this project.
        mRemoteDataSource = remoteDataSource;
    }

    /**
     * Return single instance of repository.
     *
     * @param remoteDataSource remote data source.
     * @return {@link MoviesRepository} instance.
     */
    public static synchronized MoviesRepository getInstance(MoviesDataSource remoteDataSource) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MoviesRepository(null, remoteDataSource);
            }
        }
        return sInstance;
    }

    @Override
    public Single<List<Movie>> getTopRated() {
        return mRemoteDataSource.getTopRated();
    }

    @Override
    public Single<List<Movie>> getSimilar(String movieId) {
        return mRemoteDataSource.getSimilar(movieId);
    }
}

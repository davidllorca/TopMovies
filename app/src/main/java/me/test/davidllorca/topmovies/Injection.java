package me.test.davidllorca.topmovies;

import me.test.davidllorca.topmovies.data.MoviesRepository;
import me.test.davidllorca.topmovies.data.remote.MoviesRemoteDataSource;

/**
 * Dependency injector.
 */
public class Injection {

    /**
     * Provides implementation of {@link me.test.davidllorca.topmovies.data.MoviesRepository}.
     *
     * @return MoviesRepository
     */
    public static MoviesRepository provideMoviesRepository() {
        return MoviesRepository
                .getInstance(MoviesRemoteDataSource.getInstance());
    }
}

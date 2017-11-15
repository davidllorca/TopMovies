package me.test.davidllorca.topmovies.ui.movielist;

import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.test.davidllorca.topmovies.data.MoviesRepository;

/**
 * Presenter's implementation of {@link MovieListFragment}.
 */
public class MovieListPresenter implements MovieListContract.Presenter {

    private static final String LOG_TAG = MovieListPresenter.class.getSimpleName();

    private MoviesRepository mRepository;

    private MovieListContract.View mView;

    public MovieListPresenter(MovieListContract.View view, MoviesRepository repository) {
        mView = view;
        mRepository = repository;
    }

    public void loadMovies(int offset) {
        mRepository.getTopRated(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> mView.append(movies),
                        throwable -> Log.e(LOG_TAG, throwable.getMessage())); // TODO UI BLANK
        // SCREEN
    }

    @Override
    public void loadMovies(int targetMovieId, int offset) {
        mRepository.getSimilar(targetMovieId, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> mView.append(movies),
                        throwable -> Log.e(LOG_TAG, throwable.getMessage()));
    }
}

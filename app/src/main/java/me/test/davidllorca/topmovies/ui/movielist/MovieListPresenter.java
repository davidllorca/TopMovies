package me.test.davidllorca.topmovies.ui.movielist;

import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.test.davidllorca.topmovies.data.MoviesRepository;

/**
 * Presenter's implementation of {@link MovieListActivity}.
 */
public class MovieListPresenter implements MovieListContract.Presenter {

    private static final String LOG_TAG = MovieListPresenter.class.getSimpleName();

    private MoviesRepository mRepository;

    private MovieListContract.View mView;

    public MovieListPresenter(MovieListContract.View view, MoviesRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void loadMovies() {
        mRepository.getTopRated()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> mView.showMovies(movies),
                        throwable -> Log.e(LOG_TAG, throwable.getMessage())); // TODO UI BLANK
        // SCREEN
    }
}

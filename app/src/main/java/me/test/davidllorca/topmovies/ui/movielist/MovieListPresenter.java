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

    private MovieListContract.View mView;

    private MoviesRepository mRepository;

    MovieListPresenter(MovieListContract.View view, MoviesRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void loadMovies(int offset) {
        mRepository.getTopRated(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> mView.showMovies(movies),
                        throwable -> Log.e(LOG_TAG, throwable.getMessage()));
    }

    @Override
    public void loadSimilarMovies(int targetMovieId, int offset) {
        mRepository.getSimilar(targetMovieId, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> mView.showMovies(movies),
                        throwable -> Log.e(LOG_TAG, throwable.getMessage()));
    }

}

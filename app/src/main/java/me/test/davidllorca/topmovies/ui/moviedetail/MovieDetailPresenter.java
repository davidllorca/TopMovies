package me.test.davidllorca.topmovies.ui.moviedetail;

import me.test.davidllorca.topmovies.data.MoviesRepository;

/**
 * Presenter's implementation of {@link MovieDetailActivity}.
 */
public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private static final String LOG_TAG = MovieDetailPresenter.class.getSimpleName();

    private MovieDetailContract.View mView;

    private MoviesRepository mRepository;

    public MovieDetailPresenter(MovieDetailContract.View view, MoviesRepository repository) {
        this.mView = view;
        this.mRepository = repository;
    }

    @Override
    public void loadMovie() {
        // TODO
    }

}

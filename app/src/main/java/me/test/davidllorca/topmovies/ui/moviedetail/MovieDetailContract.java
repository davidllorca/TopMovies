package me.test.davidllorca.topmovies.ui.moviedetail;

import me.test.davidllorca.topmovies.data.model.Movie;

/**
 * Contract between the View and the Presenter.
 */
public interface MovieDetailContract {

    interface View {

        void showMovie(Movie movie);

    }

    interface Presenter {

        void loadMovie();

    }

}
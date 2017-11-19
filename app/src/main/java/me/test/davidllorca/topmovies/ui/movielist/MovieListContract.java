package me.test.davidllorca.topmovies.ui.movielist;

import java.util.List;

import me.test.davidllorca.topmovies.data.model.Movie;

/**
 * Contract between the View and the Presenter.
 */
public interface MovieListContract {

    interface View {

        void showMovies(List<Movie> movies);

    }

    interface Presenter {

        void loadMovies(int offset);

        void loadSimilarMovies(int targetMovieId, int offset);
    }

}

package me.test.davidllorca.topmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.test.davidllorca.topmovies.R;
import me.test.davidllorca.topmovies.data.model.Movie;
import me.test.davidllorca.topmovies.ui.moviedetail.MovieDetailActivity;
import me.test.davidllorca.topmovies.ui.movielist.MovieListFragment;

public class HomeActivity extends AppCompatActivity implements MovieListFragment
        .OnMovieListFragmentListener.OnClick, MovieListFragment.OnMovieListFragmentListener
        .OnLoading {

    /* VIEWS */
    @BindView(R.id.pb_activity_home)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = MovieDetailActivity.getIntentByMovie(this, movie);
        startActivity(intent);
    }

    @Override
    public void onLoading(boolean isLoading) {
        mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

}

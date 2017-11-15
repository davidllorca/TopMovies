package me.test.davidllorca.topmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.test.davidllorca.topmovies.R;
import me.test.davidllorca.topmovies.data.model.Movie;
import me.test.davidllorca.topmovies.ui.moviedetail.MovieDetailActivity;
import me.test.davidllorca.topmovies.ui.movielist.MovieListFragment;

public class HomeActivity extends AppCompatActivity implements MovieListFragment
        .OnMovieListFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    @Override
    public void onClickItem(Movie movie) {
        Intent intent = MovieDetailActivity.getIntentByMovie(this, movie);
        startActivity(intent);
    }
}

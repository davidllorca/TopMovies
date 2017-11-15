package me.test.davidllorca.topmovies.ui.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.test.davidllorca.topmovies.BuildConfig;
import me.test.davidllorca.topmovies.Injection;
import me.test.davidllorca.topmovies.R;
import me.test.davidllorca.topmovies.data.model.Movie;
import me.test.davidllorca.topmovies.ui.HomeActivity;
import me.test.davidllorca.topmovies.ui.movielist.MovieListFragment;

/**
 * An activity representing a single Movie detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link HomeActivity}. // TODO
 */
public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View,
        MovieListFragment.OnMovieListFragmentListener {

    public static final String MOVIE_KEY = "movie";

    @BindView(R.id.toolbar_layout_movie_detail)
    CollapsingToolbarLayout mAppBarLayout;
    @BindView(R.id.toolbar_movie_detail)
    Toolbar mToolbar;
    @BindView(R.id.img_movie_detail_app_bar)
    ImageView mPosterImageView;
    @BindView(R.id.tv_movie_detail_overview)
    TextView mOverViewTextView;

    private Movie mItem;

    private MovieDetailPresenter mPresenter;

    public static Intent getIntentByMovie(Context context, Movie movie) {
        Bundle extras = new Bundle();
        extras.putParcelable(MovieDetailActivity.MOVIE_KEY, movie);
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        // Init toolbar
        setSupportActionBar(mToolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mPresenter = new MovieDetailPresenter(this, Injection.provideMoviesRepository());

        mItem = getIntent().getExtras().getParcelable(MovieDetailActivity.MOVIE_KEY);

        showMovie(mItem);
        loadSimilarMoviesFragment(mItem);
    }

    private void loadSimilarMoviesFragment(Movie item) {
        Fragment fragment = MovieListFragment.newInstanceAsLinearLayout(item);
        getSupportFragmentManager().beginTransaction()
//                .add(R.id.container_movie_detail_footer, fragment)
                .replace(R.id.container_movie_detail_footer, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, HomeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @UiThread
    @Override
    public void showMovie(@NonNull Movie movie) {
        if (mAppBarLayout != null) {
            mAppBarLayout.setTitle(movie.getTitle());
            Picasso.with(this)
                    .load(BuildConfig.BASE_IMAGE_URL + movie.getPosterPath())
                    .into(mPosterImageView);
            mOverViewTextView.setText(movie.getOverview());
        }
    }

    @Override
    public void onClickItem(Movie movie) {
        showMovie(movie);
        loadSimilarMoviesFragment(movie);
    }
}

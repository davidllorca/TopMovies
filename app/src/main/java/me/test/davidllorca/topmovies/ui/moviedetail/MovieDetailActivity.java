package me.test.davidllorca.topmovies.ui.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.test.davidllorca.topmovies.BuildConfig;
import me.test.davidllorca.topmovies.R;
import me.test.davidllorca.topmovies.data.model.Movie;
import me.test.davidllorca.topmovies.ui.movielist.MovieListFragment;
import me.test.davidllorca.topmovies.utils.Injection;
import me.test.davidllorca.topmovies.utils.TopMoviesUtils;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View,
        MovieListFragment.OnMovieListFragmentListener.OnClick, MovieListFragment
                .OnMovieListFragmentListener.OnLoading, MovieListFragment
                .OnMovieListFragmentListener.OnScroll, AppBarLayout.OnOffsetChangedListener {

    public static final String MOVIE_KEY = "movie";

    /* VIEWS */
    @BindView(R.id.app_bar_movie_detail)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.toolbar_layout_movie_detail)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.toolbar_movie_detail)
    Toolbar mToolbar;
    @BindView(R.id.img_movie_detail_app_bar)
    ImageView mPosterImageView;
    @BindView(R.id.tv_movie_detail_average)
    TextView mAverageTextView;
    @BindView(R.id.tv_movie_detail_overview)
    TextView mOverViewTextView;
    @BindView(R.id.img_left_arrow_icon_movie_detail_carousel)
    ImageView mLeftArrow;
    @BindView(R.id.img_right_arrow_icon_movie_detail_carousel)
    ImageView mRightArrow;
    @BindView(R.id.pb_movie_detail_carousel)
    ProgressBar mCarouselProgressBar;

    private Movie mMovie;

    /**
     * Implementation of {@link MovieDetailContract.Presenter}
     */
    private MovieDetailPresenter mPresenter;


    // Variables to handle events when expand/collapse appbar.
    private int mMaxScrollSize;
    private boolean mAverageIsHidden;

    /**
     * Get Intent to {@link MovieDetailActivity} with {@link Movie} in its arguments.
     *
     * @param context
     * @param movie   Movie to show in detail.
     * @return Intent
     */
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
        mAppbarLayout.addOnOffsetChangedListener(this);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Init presenter.
        mPresenter = new MovieDetailPresenter(this, Injection.provideMoviesRepository());

        mMovie = getIntent().getExtras().getParcelable(MovieDetailActivity.MOVIE_KEY);

        showMovie(mMovie);
        loadSimilarMoviesFragment(mMovie);
    }

    /**
     * Load movie carousel with similar movies of movie in detail.
     * @param item
     */
    private void loadSimilarMoviesFragment(Movie item) {
        Fragment fragment = MovieListFragment.newInstanceAsLinearLayout(item);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_movie_detail_footer, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @UiThread
    @Override
    public void showMovie(@NonNull Movie movie) {
        if (mCollapsingToolbarLayout != null) {
            mAppbarLayout.setExpanded(true, true);
            mCollapsingToolbarLayout.setTitle(movie.getTitle());
            mAverageTextView.setText(TopMoviesUtils.getRoundedAverageStr(movie.getVoteAverage()));
            Picasso.with(this)
                    .load(BuildConfig.BASE_IMAGE_URL + movie.getPosterPath())
                    .error(R.drawable.img_placeholder)
                    .into(mPosterImageView);
            mOverViewTextView.setText(movie.getOverview());
        }
    }

    @Override
    public void onMovieClicked(Movie movie) {
        showMovie(movie);
        loadSimilarMoviesFragment(movie);
    }

    /**
     * Shows progress bar on loading operations.
     *
     * @param isLoading true on loading, false on finish loading task.
     */
    @Override
    public void onLoading(boolean isLoading) {
        mCarouselProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    /**
     * Shows/Hides arrow icons of carousel.
     *
     * @param isFirstItemVisible
     * @param isLastItemVisible
     */
    @Override
    public void onItemVisibilityStates(boolean isFirstItemVisible, boolean isLastItemVisible) {
        mLeftArrow.setVisibility(isFirstItemVisible ? View.GONE : View.VISIBLE);
        mRightArrow.setVisibility(isLastItemVisible ? View.GONE : View.VISIBLE);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int currentScrollPercentage = (Math.abs(verticalOffset)) * 100
                / mMaxScrollSize;

        if (currentScrollPercentage >= 50) {
            if (!mAverageIsHidden) {
                mAverageIsHidden = true;

                ViewCompat.animate(mAverageTextView).scaleY(0).scaleX(0).start();
            }
        }

        if (currentScrollPercentage < 50) {
            if (mAverageIsHidden) {
                mAverageIsHidden = false;
                ViewCompat.animate(mAverageTextView).scaleY(1).scaleX(1).start();
            }
        }
    }
}

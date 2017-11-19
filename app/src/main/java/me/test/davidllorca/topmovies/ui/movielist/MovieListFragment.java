package me.test.davidllorca.topmovies.ui.movielist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.test.davidllorca.topmovies.R;
import me.test.davidllorca.topmovies.data.model.Movie;
import me.test.davidllorca.topmovies.ui.EndlessRecyclerViewScrollListener;
import me.test.davidllorca.topmovies.utils.Injection;

public class MovieListFragment extends Fragment implements MovieListContract.View,
        MovieItemAdapter.OnMovieItemAdapterListener {

    private static final String TYPE_LAYOUT_KEY = "type_layout_list";
    private static final int TYPE_GRID_LAYOUT = 0;
    private static final int TYPE_LINEAR_LAYOUT = 1;

    private static final String N_COLUMNS_KEY = "n_columns_key";
    private static final String TARGET_MOVIE_KEY = "target_movie";

    /* VIEWS */
    @BindView(R.id.rv_fragment_movie_list)
    RecyclerView mList;


    /**
     * Implementation of {@link MovieListContract.Presenter}
     */
    private MovieListPresenter mPresenter;

    /**
     * Adapter of RecyclerView.
     */
    private MovieItemAdapter mAdapter;

    /*
     * Callbacks to fragment host.
     */

    private OnMovieListFragmentListener.OnClick mHostClickListener;
    private OnMovieListFragmentListener.OnLoading mHostLoadingListener;
    private OnMovieListFragmentListener.OnScroll mHostScrollListener;

    /**
     * Target movie to load similar movies.
     */
    private Movie mTargetMovie;

    public MovieListFragment() {
    }

    /**
     * Constructor with params to set LinearLayout configuration.
     *
     * @param movie Target movie.
     * @return MovieListFragment.
     */
    public static MovieListFragment newInstanceAsLinearLayout(Movie movie) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_LAYOUT_KEY, TYPE_LINEAR_LAYOUT);
        args.putParcelable(TARGET_MOVIE_KEY, movie);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Constructor with params to set GridLayout configuration. It's the default Configuration.
     *
     * @param nColumns Numbers of column's grid.
     * @return MovieListFragment.
     */
    public static MovieListFragment newInstanceAsGridLayout(int nColumns) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_LAYOUT_KEY, TYPE_GRID_LAYOUT);
        args.putInt(N_COLUMNS_KEY, nColumns);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init presenter
        mPresenter = new MovieListPresenter(this, Injection.provideMoviesRepository());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments == null) {
            // Default setup as gridlayout.
            setupRecyclerView(mList, getResources().getInteger(R.integer
                    .movie_list_column_count_default));
        } else {
            mTargetMovie = arguments.getParcelable(TARGET_MOVIE_KEY);
            setupRecyclerView(mList, arguments.getInt(N_COLUMNS_KEY, 1));
        }

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, @Nullable
            int nColumns) {
        RecyclerView.LayoutManager layoutManager = nColumns > 1 ?
                new GridLayoutManager(getContext(), nColumns) :
                new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Implementation of ScrollListener with loading callback events.
        EndlessRecyclerViewScrollListener mScrollListener = new EndlessRecyclerViewScrollListener
                (layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextData(page);
            }

            @Override
            public void onItemVisibilityStates(boolean isFirstItemVisible, boolean
                    isLastItemVisible) {
                if (mHostScrollListener != null)
                    mHostScrollListener.onItemVisibilityStates(isFirstItemVisible,
                            isLastItemVisible);
            }
        };
        recyclerView.addOnScrollListener(mScrollListener);

        mAdapter = new MovieItemAdapter(getContext(), nColumns > 1 ?
                MovieItemAdapter.GRID_LAYOUT : MovieItemAdapter.LINEAR_HORIZONTAL_LAYOUT,
                this);

        // If target movie exists showMovies it to data set.
        if (mTargetMovie != null) mAdapter.append(mTargetMovie);

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter == null || mAdapter.getItemCount() == 0)
            loadNextData(1);
    }


    public void loadNextData(int offset) {
        if (mHostLoadingListener != null) mHostLoadingListener.onLoading(true);
        if (mTargetMovie == null) {
            mPresenter.loadMovies(offset);
        } else {
            mPresenter.loadSimilarMovies(mTargetMovie.getId(), offset);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Just OnClick listener is mandatory.
        if (context instanceof OnMovieListFragmentListener.OnClick) {
            mHostClickListener = (OnMovieListFragmentListener.OnClick) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMovieListFragmentListener.OnClick");
        }
        if (context instanceof OnMovieListFragmentListener.OnLoading) {
            mHostLoadingListener = (OnMovieListFragmentListener.OnLoading) context;
        }
        if (context instanceof OnMovieListFragmentListener.OnScroll) {
            mHostScrollListener = (OnMovieListFragmentListener.OnScroll) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHostClickListener = null;
        mHostLoadingListener = null;
        mHostScrollListener = null;
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mAdapter.appendAll(movies);
        if (mHostLoadingListener != null) mHostLoadingListener.onLoading(false);
    }

    @Override
    public void onClickItem(Movie movie) {
        if (mHostClickListener != null) mHostClickListener.onMovieClicked(movie);
    }


    /**
     * Interfaces between {@link MovieListFragment} and its host.
     */
    public interface OnMovieListFragmentListener {

        interface OnClick {

            /**
             * Called on click event on collection.
             */
            void onMovieClicked(Movie movie);

        }

        interface OnLoading {

            /**
             * Called on start/end loading operations.
             */
            void onLoading(boolean isLoading);

        }

        interface OnScroll {

            /**
             * Called on scroll events.
             */
            void onItemVisibilityStates(boolean isFirstItemVisible, boolean isLastItemVisible);

        }

    }

}

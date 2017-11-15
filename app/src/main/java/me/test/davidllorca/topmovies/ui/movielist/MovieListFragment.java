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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.test.davidllorca.topmovies.BuildConfig;
import me.test.davidllorca.topmovies.Injection;
import me.test.davidllorca.topmovies.R;
import me.test.davidllorca.topmovies.data.model.Movie;
import me.test.davidllorca.topmovies.ui.EndlessRecyclerViewScrollListener;

/**
 * TODO
 */
public class MovieListFragment extends Fragment implements MovieListContract.View {

    private static final String TYPE_GRID_LAYOUT = "type_grid_layout";
    private static final String TYPE_LINEAR_LAYOUT = "type_linear_layout";
    private static final String N_COLUMNS_KEY = "n_columns_key";
    private static final String TARGET_MOVIE_KEY = "target_movie";

    @BindView(R.id.rv_fragment_movie_list)
    RecyclerView mList;

    private MovieListPresenter mPresenter;

    private Movie mTargetItem;

    private MovieItemRecyclerViewAdapter mAdapter;
    private EndlessRecyclerViewScrollListener mScrollListener;

    private OnMovieListFragmentListener mListener;

    public MovieListFragment() {
    }

    public static MovieListFragment newInstanceAsGridLayout(int nColumns) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putBoolean(TYPE_GRID_LAYOUT, true);
        args.putInt(N_COLUMNS_KEY, nColumns);
        fragment.setArguments(args);
        return fragment;
    }

    public static MovieListFragment newInstanceAsLinearLayout(Movie item) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putBoolean(TYPE_LINEAR_LAYOUT, true);
        args.putParcelable(TARGET_MOVIE_KEY, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            setupRecyclerView(mList, getResources().getInteger(R.integer
                    .movie_list_column_count_default));
        } else {
            mTargetItem = arguments.getParcelable(TARGET_MOVIE_KEY);
            setupRecyclerView(mList, arguments.getInt(N_COLUMNS_KEY, 1));
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, @Nullable
            int nColumns) {
        RecyclerView.LayoutManager layoutManager = nColumns > 1 ?
                new GridLayoutManager(getContext(), nColumns) :
                new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextData(page);
            }
        };
        recyclerView.addOnScrollListener(mScrollListener);

        if (mTargetItem != null) {
            mAdapter = new MovieItemRecyclerViewAdapter(getContext(), mTargetItem, mListener);
        } else {
            mAdapter = new MovieItemRecyclerViewAdapter(getContext(), mListener);
        }

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter == null || mAdapter.getItemCount() == 0)
            loadNextData(1);
    }

    // TODO CUSTOMIZE
    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextData(int offset) {
        append(getMockMovies());
        //        if (mTargetItem == null) {
//            mPresenter.loadMovies(offset);
//        } else {
//            mPresenter.loadMovies(mTargetItem.getId(), offset);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMovieListFragmentListener) {
            mListener = (OnMovieListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMovieListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mAdapter.loadData(movies);
    }

    @Override
    public void append(List<Movie> movies) {
        mAdapter.append(movies);
    }

    //TODO remove on release
    private List<Movie> getMockMovies() {
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(getResources()
                .openRawResource(R.raw.data));
        Type type = new TypeToken<List<Movie>>() {
        }.getType();
        return gson.fromJson(reader, type);
    }

    public interface OnMovieListFragmentListener {
        void onClickItem(Movie movie);
    }

    public static class MovieItemRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieItemRecyclerViewAdapter.ViewHolder> {

        private final List<Movie> mDataSet;
        private final Context mContext;
        private final OnMovieListFragmentListener mListener;

        MovieItemRecyclerViewAdapter(Context context, OnMovieListFragmentListener listener) {
            mContext = context;
            mDataSet = new ArrayList<>();
            mListener = listener;
        }

        MovieItemRecyclerViewAdapter(Context context, Movie targetItem,
                                     OnMovieListFragmentListener listener) {
            mContext = context;
            mDataSet = new ArrayList<>();
            mDataSet.add(targetItem);
            mListener = listener;
        }

        @Override
        public MovieItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int
                viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_item, parent, false);
            return new MovieItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MovieItemRecyclerViewAdapter.ViewHolder holder, int
                position) {
            Movie item = mDataSet.get(position);
            holder.mTitle.setText(item.getTitle());
//            holder.mAverage.setText(item.getVoteAverage());TODO
            Picasso.with(mContext)
                    .load(BuildConfig.BASE_IMAGE_URL + item.getPosterPath())
                    .into(holder.mPoster);

            holder.itemView.setOnClickListener(v -> {
//                if (mTwoPane) {// TODO TABLE UI
//                    Bundle arguments = new Bundle();
//                    arguments.putParcelable(MovieDetailActivity.MOVIE_KEY, item);
//                    MovieDetailFragment fragment = new MovieDetailFragment();
//                    fragment.setArguments(arguments);
//                    mParentActivity.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.view_movie_detail_overview, fragment)
//                            .commit();
//                } else {
//                    Context context = v.getContext();
//                    Intent intent = MovieDetailActivity.getIntentByMovie(context, item);
//                    context.startActivity(intent);
//                }
                mListener.onClickItem(item);
            });
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

        public void loadData(List<Movie> items) {
            mDataSet.clear();
            mDataSet.addAll(items);
            notifyDataSetChanged();
        }

        public void append(List<Movie> items) {
            mDataSet.addAll(items);
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_movie_item_title)
            TextView mTitle;
            @BindView(R.id.img_movie_item_poster)
            ImageView mPoster;
            @BindView(R.id.img_btn_tv_movie_item_average)
            ImageButton mAverage;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}

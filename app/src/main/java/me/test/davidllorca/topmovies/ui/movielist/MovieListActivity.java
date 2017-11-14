package me.test.davidllorca.topmovies.ui.movielist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import me.test.davidllorca.topmovies.ui.moviedetail.MovieDetailActivity;
import me.test.davidllorca.topmovies.ui.moviedetail.MovieDetailFragment;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity implements MovieListContract.View {

    /* VIEWS */
    @BindView(R.id.toolbar_movie_list)
    Toolbar mToolbar;
    @BindView(R.id.rv_movie_list)
    RecyclerView mList;

    private MovieListPresenter mPresenter;

    private MovieItemRecyclerViewAdapter mAdapter;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mPresenter = new MovieListPresenter(this, Injection.provideMoviesRepository());

        if (findViewById(R.id.view_movie_detail_overview) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        setupRecyclerView(mList);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(
                new GridLayoutManager(this, getResources().getInteger(R.integer
                        .movie_list_column_count)));

//        mAdapter = new MovieItemRecyclerViewAdapter(this, getMockMovies(), mTwoPane);
        mAdapter = new MovieItemRecyclerViewAdapter(this, mTwoPane);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadMovies();
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mAdapter.loadData(movies);
    }

    /**
     * Generates mock data.
     *
     * @return List<Member> of mock members.
     */
    private List<Movie> getMockMovies() {
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(getResources()
                .openRawResource(R.raw.data));
        Type type = new TypeToken<List<Movie>>() {
        }.getType();
        return gson.fromJson(reader, type);
    }

    public static class MovieItemRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieItemRecyclerViewAdapter.ViewHolder> {

        private final MovieListActivity mParentActivity;
        private final List<Movie> mDataSet;
        private final boolean mTwoPane;

        MovieItemRecyclerViewAdapter(MovieListActivity parent, boolean twoPane) {
            mDataSet = new ArrayList<>();
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        MovieItemRecyclerViewAdapter(MovieListActivity parent, List<Movie> dataSet, boolean
                twoPane) {
            mDataSet = dataSet;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Movie item = mDataSet.get(position);
            holder.mTitle.setText(item.getTitle());
//            holder.mAverage.setText(item.getVoteAverage());TODO
            Picasso.with(mParentActivity)
                    .load(BuildConfig.BASE_IMAGE_URL + item.getPosterPath())
                    .into(holder.mPoster);

            holder.itemView.setOnClickListener(v -> {
                if (mTwoPane) {// TODO TABLE UI
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(MovieDetailActivity.MOVIE, item);
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.view_movie_detail_overview, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = MovieDetailActivity.getIntentByMovie(context, item);
                    context.startActivity(intent);
                }
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

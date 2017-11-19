package me.test.davidllorca.topmovies.ui.movielist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.test.davidllorca.topmovies.BuildConfig;
import me.test.davidllorca.topmovies.R;
import me.test.davidllorca.topmovies.data.model.Movie;
import me.test.davidllorca.topmovies.utils.TopMoviesUtils;

import static android.graphics.Typeface.BOLD;

/**
 * Adapter to bind {@link Movie} items
 */
public class MovieItemAdapter
        extends RecyclerView.Adapter<MovieItemAdapter.ViewHolder> {

    public static final int LINEAR_HORIZONTAL_LAYOUT = 0;
    public static final int GRID_LAYOUT = 1;

    private final Context mContext;
    private final List<Movie> mDataSet;
    private final OnMovieItemAdapterListener mListener;
    private int mItemLayoutResource;

    MovieItemAdapter(Context context, int type,
                     OnMovieItemAdapterListener listener) {
        mContext = context;
        mDataSet = new ArrayList<>();
        mListener = listener;
        setItemLayout(type);
    }

    private void setItemLayout(int type) {
        switch (type) {
            case LINEAR_HORIZONTAL_LAYOUT:
                mItemLayoutResource = R.layout.movie_list_item_horizontal;
                break;
            default:
                mItemLayoutResource = R.layout.movie_list_item_vertical;
                break;
        }
    }

    @Override
    public MovieItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int
            viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(mItemLayoutResource, parent, false);
        return new MovieItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieItemAdapter.ViewHolder holder, int
            position) {
        Movie item = mDataSet.get(position);

        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(item.getTitle() + " - ");
        int start = sb.length();
        sb.append(TopMoviesUtils.getRoundedAverageStr(item.getVoteAverage()));
        sb.setSpan(new StyleSpan(BOLD), start, sb
                .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.mTitle.setText(sb);

        Picasso.with(mContext)
                .load(BuildConfig.BASE_IMAGE_URL + item.getPosterPath())
                .error(R.drawable.img_placeholder)
                .into(holder.mPoster);

        holder.itemView.setOnClickListener(v -> mListener.onClickItem(item));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void append(Movie item) {
        mDataSet.add(item);
        notifyDataSetChanged();
    }

    public void appendAll(List<Movie> items) {
        int initPos = mDataSet.size();
        mDataSet.addAll(items);
        notifyItemRangeInserted(initPos, items.size());
    }

    public interface OnMovieItemAdapterListener {
        /**
         * Called on click event on item collection.
         */
        void onClickItem(Movie movie);
    }

    /**
     * Model of ViewHolder.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_movie_item_title)
        TextView mTitle;
        @BindView(R.id.img_movie_item_poster)
        ImageView mPoster;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
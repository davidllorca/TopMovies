package me.test.davidllorca.topmovies.ui.moviedetail;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.test.davidllorca.topmovies.BuildConfig;
import me.test.davidllorca.topmovies.R;
import me.test.davidllorca.topmovies.data.model.Movie;

/**
 * TODO
 */
public class MovieDetailFragment extends Fragment {

    private Movie mItem;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mItem = getArguments().getParcelable(MovieDetailActivity.MOVIE_KEY);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity
                    .findViewById(R.id.toolbar_layout_movie_detail);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTitle());
                ImageView poster = appBarLayout.findViewById(R.id.img_movie_detail_app_bar);
                Picasso.with(getContext())
                        .load(BuildConfig.BASE_IMAGE_URL + mItem.getPosterPath())
                        .into(poster);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.movie_detail)).setText(mItem.getOverview());
        }

        return rootView;
    }
}

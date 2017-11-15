package me.test.davidllorca.topmovies.data.remote;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Movie End-points.
 */
public interface MovieService {

    /**
     * {@see https://developers.themoviedb.org/3/movies/get-top-rated-movies }
     */
    @GET("movie/top_rated")
    Single<MovieListResponse> getTopRated(@Query("page") int offset);

    /**
     * {@see https://developers.themoviedb.org/3/movies/get-similar-movies }
     */
    @GET("movie/{movie_id}/similar")
    Single<MovieListResponse> getSimilar(@Path("movie_id") int movieId, @Query("page") int
            offset);



}

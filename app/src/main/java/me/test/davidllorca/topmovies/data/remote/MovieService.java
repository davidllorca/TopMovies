package me.test.davidllorca.topmovies.data.remote;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Movie End-points.
 */
public interface MovieService {

    @GET("movie/top_rated")
    Single<MovieListResponse> getTopRated(); //@Query("page") int page);//TODO

    @GET("movie/{movie_id}/similar")
    Single<MovieListResponse> getSimilar(@Path("movie_id") String movieId);//@Query("page") int
    // page);//TODO

}

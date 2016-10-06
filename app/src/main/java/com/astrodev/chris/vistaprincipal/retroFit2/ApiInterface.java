package com.astrodev.chris.vistaprincipal.retroFit2;

/**
 * Created by christopher on 6/10/16.
 */
import com.astrodev.chris.vistaprincipal.retroFit2.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/top_rated") Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}") Call<MovieResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}

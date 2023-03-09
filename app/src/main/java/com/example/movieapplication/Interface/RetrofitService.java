package com.example.movieapplication.Interface;

import com.example.movieapplication.Model.MovieResponse;
import com.example.movieapplication.Model.PersonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("search/movie")
    Call<MovieResponse> getMovieByQuery(@Query("api_key") String api_key, @Query("query") String query);

    @GET("search/person")
    Call<PersonResponse> getActorByQuery(@Query("api_key") String api_key, @Query("query") String query);


}

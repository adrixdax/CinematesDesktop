package sample.retrofit;

import component.db.Reviews;
import component.films.Film;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface RetrofitInterface {

    @POST("/film")
    Call<List<Film>> getFilm(@Body String body);

    @POST("/film")
    Call<List<Film>> getFilmById(@Body String body);

    @POST("/notify")
    Call<String> createNotify(@Body String body);

    @GET("/review")
    Call<List<Reviews>> getSingleReview(@Query("id_review") String idReview);

    @POST("/review")
    Call<List<Reviews>> getReview(@Body String body);

}
package sample.retrofit;

import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitSingleton {

    private final static String url = "http://aboutactingapes.ddns.net:8080";
    private static retrofit2.Retrofit retrofit;

    public static retrofit2.Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder().baseUrl(url).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(JacksonConverterFactory.create()).build();
        }
        return retrofit;
    }

}

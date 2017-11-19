package me.test.davidllorca.topmovies.data.remote;

import java.util.concurrent.TimeUnit;

import me.test.davidllorca.topmovies.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Helper class to instantiate services.
 */
class RetrofitHelper {

    static <T> T createRetrofitService(final Class<T> service) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    HttpUrl url = request.url().newBuilder()
                            .addQueryParameter("api_key", BuildConfig.API_KEY)
                            .build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                })
                //Uncomment to debug
//                .addInterceptor(new HttpLoggingInterceptor()
//                        .setLevel(HttpLoggingInterceptor
//                                .Level.BODY))
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS);

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(BuildConfig.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();

        return retrofit.create(service);
    }

}

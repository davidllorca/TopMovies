package me.test.davidllorca.topmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.test.davidllorca.topmovies.data.MoviesRepository;
import me.test.davidllorca.topmovies.data.remote.MovieService;
import me.test.davidllorca.topmovies.data.remote.MoviesRemoteDataSource;
import me.test.davidllorca.topmovies.data.remote.RetrofitHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MoviesRemoteDataSource dataSource = new MoviesRemoteDataSource();
        MoviesRepository rep = MoviesRepository.getInstance(dataSource);

        rep.getTopRated()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> {
                    movies.toString();
                }, throwable -> Log.e("Response", throwable.getMessage()));// TODO ERROR HANDLING

    }

}

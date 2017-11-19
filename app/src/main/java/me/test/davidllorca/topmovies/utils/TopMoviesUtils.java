package me.test.davidllorca.topmovies.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import me.test.davidllorca.topmovies.R;
import me.test.davidllorca.topmovies.data.model.Movie;

/**
 * Helper methods of app use cases.
 */
public class TopMoviesUtils {

    /**
     * Get String value of average with format #.#
     *
     * @param average
     * @return String value.
     */
    public static String getRoundedAverageStr(double average) {
        BigDecimal bd = new BigDecimal(Double.toString(average));
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return String.valueOf(bd.doubleValue());
    }

    /**
     * Just for testing and develope.
     *
     * @return Get mock list of movies.
     */
    private List<Movie> getMockMovies(Context context) {
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(context.getResources()
                .openRawResource(R.raw.data));
        Type type = new TypeToken<List<Movie>>() {
        }.getType();
        return gson.fromJson(reader, type);
    }
}

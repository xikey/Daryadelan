package daryadelan.sandogh.zikey.com.daryadelan.repo.apiClient;

import android.content.Context;

import daryadelan.sandogh.zikey.com.daryadelan.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Zikey on 03/06/2017.
 */

public class ServerApiClient {


    private static Retrofit retrofitWithoutHeader = null;


    public static Retrofit getClient(Context context) {

        try {
            if (retrofitWithoutHeader == null) {
                retrofitWithoutHeader = new Retrofit.Builder()
                        .baseUrl(BuildConfig.IPAddress)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofitWithoutHeader;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public static void clearRetrofit() {


        retrofitWithoutHeader = null;
    }
}

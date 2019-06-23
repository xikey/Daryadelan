package daryadelan.sandogh.zikey.com.daryadelan.repo.apiClient;

import android.content.Context;

import java.io.IOException;

import daryadelan.sandogh.zikey.com.daryadelan.BuildConfig;
import daryadelan.sandogh.zikey.com.daryadelan.LoginActivity;
import daryadelan.sandogh.zikey.com.daryadelan.model.SessionManagement;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Zikey on 03/06/2017.
 */

public class ServerApiClient {


    private static Retrofit retrofitWithoutHeader = null;
    private static Retrofit retrofitWithHeader = null;

    private static OkHttpClient.Builder initHeader(final Context context) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Authorization", SessionManagement.getInstance(context).getToken_type()+" "+SessionManagement.getInstance(context).getToken())
                        .method(original.method(), original.body())
                        .build();

                Response response = chain.proceed(request);
                if (response.code() == 401) {
                    SessionManagement.getInstance(context).clearMemberData();
                    LoginActivity.start(context);
                    System.exit(0);
                }

                return response;
            }
        });
        return httpClient;
    }



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

    public static Retrofit getClientWithHeader(Context context) {

        try {
            if (retrofitWithHeader == null) {
                retrofitWithHeader = new Retrofit.Builder()
                        .baseUrl(BuildConfig.IPAddress)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(initHeader(context).build())
                        .build();
            }
            return retrofitWithHeader;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public static void clearRetrofit() {

        retrofitWithHeader = null;
        retrofitWithoutHeader = null;
    }
}

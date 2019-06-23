package API.Base;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static Retrofit getClient() {

       // HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(10);
        dispatcher.setMaxRequestsPerHost(2);
        OkHttpClient client = new OkHttpClient.Builder().dispatcher(dispatcher).
                readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(40,TimeUnit.SECONDS).
                build();

        /*
        Deserializer deserializer = new Deserializer(Message.class,"message");

        Gson gson =
                new GsonBuilder()
                        .registerTypeAdapter(Message.class, deserializer)
                        .create();

        */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://103.76.180.69:1080")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        //   .client(client)

        return retrofit;
    }
}

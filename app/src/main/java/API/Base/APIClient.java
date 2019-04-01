package API.Base;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        /*
        Deserializer deserializer = new Deserializer(Message.class,"message");

        Gson gson =
                new GsonBuilder()
                        .registerTypeAdapter(Message.class, deserializer)
                        .create();

        */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-54-169-157-221.ap-southeast-1.compute.amazonaws.com:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}

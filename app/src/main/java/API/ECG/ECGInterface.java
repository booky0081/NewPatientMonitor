package API.ECG;

import java.util.List;

import DataModel.Count;
import DataModel.ECGModel;
import DataModel.PlainMessage;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ECGInterface {
    @GET("/ECG")
    Call<ECGModel> Get();

    @GET("/ECG/Count")
    Call<Count> Count();

    @POST("/ECG")
    Call<PlainMessage> Create(@Body List<ECGModel> ECGModels);

    @Multipart
    @POST("/ECG/upload")
    Call<PlainMessage> upload(
            @Part MultipartBody.Part file
    );

}

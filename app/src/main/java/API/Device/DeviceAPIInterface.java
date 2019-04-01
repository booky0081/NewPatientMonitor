package API.Device;

import java.util.List;

import DataModel.Count;
import DataModel.DeviceModel;
import DataModel.PatientModel;
import DataModel.PlainMessage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DeviceAPIInterface {

    @GET("/Device")
    Call<List<PatientModel>> Get();

    @GET("/Device/Count")
    Call<Count> Count();

    @POST("/Device")
    Call<PlainMessage> Create(@Body List<DeviceModel> deviceModelList);
}

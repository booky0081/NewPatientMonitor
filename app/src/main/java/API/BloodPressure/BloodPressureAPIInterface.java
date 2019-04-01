package API.BloodPressure;

import java.util.List;

import DataModel.BloodPressureModel;
import DataModel.Count;
import DataModel.PatientModel;
import DataModel.PlainMessage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BloodPressureAPIInterface {

    @GET("/BloodPressure")
    Call<PatientModel> Get();

    @GET("/BloodPressure/Count")
    Call<Count> Count();

    @POST("/BloodPressure")
    Call<PlainMessage> Create(@Body List<BloodPressureModel> bloodPressureModels);
}

package API.Hospital;

import java.util.List;

import DataModel.Count;
import DataModel.HospitalModel;
import DataModel.PatientModel;
import DataModel.PlainMessage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface HospitalAPIInterface {

    @GET("/Hospital")
    Call<List<PatientModel>> Get();

    @GET("/Hospital/Count")
    Call<Count> Count();

    @POST("/Hospital")
    Call<PlainMessage> Create(@Body List<HospitalModel> hospitalModels);
}

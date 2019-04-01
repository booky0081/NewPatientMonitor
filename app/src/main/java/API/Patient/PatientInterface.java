package API.Patient;

import java.util.List;

import DataModel.Count;
import DataModel.PatientModel;
import DataModel.PlainMessage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PatientInterface {

    @GET("/Patient")
    Call<PatientModel> Get();

    @GET("/Count")
    Call<Count> Count();

    @POST("/Patient")
    Call<PlainMessage> Create(@Body List<PatientModel> patientModels);
}

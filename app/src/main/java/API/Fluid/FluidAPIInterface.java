package API.Fluid;

import java.util.List;

import DataModel.Count;
import DataModel.FluidModel;
import DataModel.PatientModel;
import DataModel.PlainMessage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FluidAPIInterface {
    @GET("/Fluid")
    Call<PatientModel> Get();

    @GET("/Fluid/Count")
    Call<Count> Count();

    @POST("/Fluid")
    Call<PlainMessage> Create(@Body List<FluidModel> FluidModel);
}

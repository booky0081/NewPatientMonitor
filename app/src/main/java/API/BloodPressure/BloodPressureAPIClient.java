package API.BloodPressure;

import java.util.List;

import API.Base.APIClient;
import API.Base.APIClientInterface;
import API.Base.ErrorUtils;
import DataModel.APIError;
import DataModel.BloodPressureModel;
import DataModel.DeviceModel;
import DataModel.PlainMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BloodPressureAPIClient {

    private Retrofit retrofit;

    private Call<DeviceModel> getCall;

    private Call<PlainMessage> postCall;

    private BloodPressureAPIInterface bloodPressureAPIInterface;

    public BloodPressureAPIClient(){

        retrofit = APIClient.getClient();
        bloodPressureAPIInterface =retrofit.create(BloodPressureAPIInterface.class);
    }

    public void Post(List<BloodPressureModel> bloodPressureModels, APIClientInterface apiClientInterface){

        postCall = bloodPressureAPIInterface.Create(bloodPressureModels);

        postCall.enqueue(new Callback<PlainMessage>() {
            @Override
            public void onResponse(Call<PlainMessage> call, Response<PlainMessage> response) {

                if(response.isSuccessful()) {

                    apiClientInterface.onReponse();


                }else{

                    APIError error =  ErrorUtils.parseError(response,retrofit);

                    apiClientInterface.onError(error.getMessage());

                }

                apiClientInterface.onDone();

            }


            @Override
            public void onFailure(Call<PlainMessage> call, Throwable t) {

                apiClientInterface.onError("NETWORK FAILURE");

                postCall.cancel();

                apiClientInterface.onDone();
            }
        });

    }
}

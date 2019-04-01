package API.Hospital;

import java.util.List;

import API.Base.APIClient;
import API.Base.APIClientInterface;
import API.Base.ErrorUtils;
import DataModel.APIError;
import DataModel.HospitalModel;
import DataModel.PlainMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HospitalAPIClient {

    private Retrofit retrofit;

    private Call< HospitalModel> getCall;

    private Call<PlainMessage> postCall;

    private HospitalAPIInterface hospitalAPIInterface;

    public HospitalAPIClient(){

        retrofit = APIClient.getClient();
        hospitalAPIInterface =retrofit.create(HospitalAPIInterface.class);
    }

    public void Post(List<HospitalModel> hospitalModels, APIClientInterface apiClientInterface){

        postCall = hospitalAPIInterface.Create(hospitalModels);

        postCall.enqueue(new Callback<PlainMessage>() {
            @Override
            public void onResponse(Call<PlainMessage> call, Response<PlainMessage> response) {

                 if(response.code() == 200 || response.code() == 201) {

                    apiClientInterface.onReponse();

                }else{

                    APIError error =  ErrorUtils.parseError(response,retrofit);

                    apiClientInterface.onError(error.getMessage());

                }

                apiClientInterface.onDone();

            }


            @Override
            public void onFailure(Call<PlainMessage> call, Throwable t) {

                apiClientInterface.onError(t.getMessage());

                postCall.cancel();

                apiClientInterface.onDone();
            }
        });

    }
}

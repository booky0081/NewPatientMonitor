package API.Patient;

import android.util.Log;

import java.util.List;

import API.Base.APIClient;
import API.Base.APIClientInterface;
import API.Base.ErrorUtils;
import DataModel.APIError;
import DataModel.PatientModel;
import DataModel.PlainMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PatientAPIClient {

    private Retrofit retrofit;

    private Call< PatientModel> getCall;

    private Call<PlainMessage> postCall;

    private PatientInterface patientInterface;

    public PatientAPIClient(){

        retrofit = APIClient.getClient();
        patientInterface =retrofit.create(PatientInterface.class);
    }

    public void Post(List<PatientModel > patientModelList, APIClientInterface apiClientInterface){

        postCall = patientInterface.Create(patientModelList);

        postCall.enqueue(new Callback<PlainMessage>() {
            @Override
            public void onResponse(Call<PlainMessage> call, Response<PlainMessage> response) {

                Log.d("APIClient", "Patient");
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

                apiClientInterface.onError("NETWORK FAILURE");

                apiClientInterface.onDone();
            }
        });


    }
}

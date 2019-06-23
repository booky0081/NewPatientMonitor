package API.ECG;

import java.io.File;
import java.util.List;

import API.Base.APIClient;
import API.Base.APIClientInterface;
import API.Base.ErrorUtils;
import DataModel.APIError;
import DataModel.ECGModel;
import DataModel.PlainMessage;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ECGAPIClient {

    private Retrofit retrofit;

    private Call<ECGModel> getCall;

    private Call<PlainMessage> postCall;

    private ECGInterface ecgInterface;

    private Call<PlainMessage> uploadCall;


    public ECGAPIClient(){

        retrofit = APIClient.getClient();
        ecgInterface =retrofit.create(ECGInterface.class);
    }

    public void Post(List<ECGModel> ecgModelList, APIClientInterface apiClientInterface){

        postCall = ecgInterface.Create(ecgModelList);

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


    public void Upload(File file,APIClientInterface apiClientInterface){


        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file.getAbsoluteFile());

        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("record",file.getName(),requestFile);

        uploadCall = ecgInterface.upload(multipartBody);

        uploadCall.enqueue(new Callback<PlainMessage>() {
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

                uploadCall.cancel();

                apiClientInterface.onDone();
            }
        });

    }
}

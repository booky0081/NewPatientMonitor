package API.Base;

import java.io.IOException;
import java.lang.annotation.Annotation;

import DataModel.APIError;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ErrorUtils {

    public static APIError parseError (Response<?> response, Retrofit retrofit){

        Converter<ResponseBody,APIError> converter = retrofit.responseBodyConverter(APIError.class, new Annotation[0]);
        APIError error;
        try{
            error = converter.convert(response.errorBody());
        }catch(IOException err){
            return new APIError();
        }

        return error;
    }
}

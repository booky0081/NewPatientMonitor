package DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIError {

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
            return message;
        }


    public void setMessage(String message) {
            this.message = message;
        }

    @SerializedName("code")
    @Expose

    private String code;

    public String  getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}


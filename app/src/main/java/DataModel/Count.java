package DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Count {

    @SerializedName("count")
    @Expose
    private  int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

package DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Hospital")
public class HospitalModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("alias")
    @Expose
    @ColumnInfo(name = "hospital_name")
    private String hosipitalName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHosipitalName() {

        return hosipitalName;
    }

    public void setHosipitalName(String hosipitalName) {
        this.hosipitalName = hosipitalName;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    @ColumnInfo(name = "hospital_id")
    private String hospital_id;
}


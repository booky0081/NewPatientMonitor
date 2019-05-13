package DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import Helper.TimestampConverter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "BloodPressure")
@TypeConverters({TimestampConverter.class,})
public class BloodPressureModel {

    @PrimaryKey(autoGenerate = true)

    private int id;

    @SerializedName("diastolic")
    @Expose
    @ColumnInfo(name = "diastolic")

    private  long diastolic;


    @SerializedName("systolic")
    @Expose
    @ColumnInfo(name = "systolic")
    private long systolic;


    @SerializedName("pulse")
    @Expose
    @ColumnInfo(name= "pulse")
    private long pulse;


    @SerializedName("deviceId")
    @Expose
    @ColumnInfo(name = "meta_id")
    private long metaId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(long diastolic) {
        this.diastolic = diastolic;
    }

    public long getSystolic() {
        return systolic;
    }

    public void setSystolic(long systolic) {
        this.systolic = systolic;
    }

    public long getPulse() {
        return pulse;
    }

    public void setPulse(long pulse) {
        this.pulse = pulse;
    }


    public long getMetaId() {
        return metaId;
    }

    public void setMetaId(long metaId) {
        this.metaId = metaId;
    }
}

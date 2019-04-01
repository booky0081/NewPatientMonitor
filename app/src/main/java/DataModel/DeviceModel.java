package DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import Helper.TimestampConverter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


@Entity (indices = {@Index(value = {"patient_id"}
        )})

public class DeviceModel {


    @PrimaryKey(autoGenerate = true)

    private int id;

    @SerializedName("deviceName")
    @Expose
    @ColumnInfo(name = "device_name")
    private String deviceName;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    @ColumnInfo(name = "device_id")
    private int deviceId;

    @SerializedName("patientId")
    @Expose
    @ColumnInfo(name = "patient_id")
    private long patientId;

    @SerializedName("startDate")
    @Expose
    @ColumnInfo(name = "created_date")
    @TypeConverters({TimestampConverter.class})
    private Date createDate;


    @SerializedName("stopDate")
    @Expose
    @TypeConverters({TimestampConverter.class})
    @ColumnInfo(name = "stop_date")
    private Date stopDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }
}



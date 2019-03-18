package DataModel;

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

    @ColumnInfo(name = "device_name")
    private String deviceName;

    @ColumnInfo(name = "patient_id")
    private int patientId;

    @ColumnInfo(name = "created_date")
    @TypeConverters({TimestampConverter.class})
    private Date createDate;

    @ColumnInfo(name = "stop_date")
    @TypeConverters({TimestampConverter.class})
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

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
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



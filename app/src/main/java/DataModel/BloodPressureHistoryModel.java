package DataModel;

import java.util.Date;

import Helper.TimestampConverter;
import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

public class BloodPressureHistoryModel {


    @ColumnInfo(name = "diastolic")

    private  long diastolic;

    @ColumnInfo(name = "systolic")

    private long systolic;

    @ColumnInfo(name= "pulse")

    private long pulse;

    @ColumnInfo(name = "hospital_name")
    private String hosipitalName;

    @ColumnInfo(name = "created_date")
    @TypeConverters({TimestampConverter.class})
    private Date createDate;


    @TypeConverters({TimestampConverter.class})
    @ColumnInfo(name = "stop_date")
    private Date stopDate;

    @ColumnInfo(name = "first_name")
    private String firstName;


    @ColumnInfo(name = "device_name")
    private String deviceName;

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

    public String getHosipitalName() {
        return hosipitalName;
    }

    public void setHosipitalName(String hosipitalName) {
        this.hosipitalName = hosipitalName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}

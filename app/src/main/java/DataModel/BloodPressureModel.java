package DataModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "BloodPressure",indices = {@Index(value = {"device_id"}
)})

public class BloodPressureModel {

    @PrimaryKey(autoGenerate = true)

    private int id;

    @ColumnInfo(name = "device_id")

    private String deviceId;

    public int getId() {
        return id;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public void setId(int id) {
        this.id = id;
    }

    private long patientId ;



    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}

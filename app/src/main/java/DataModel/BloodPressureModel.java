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

    private int deviceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
}

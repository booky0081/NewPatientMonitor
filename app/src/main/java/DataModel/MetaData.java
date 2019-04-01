package DataModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "MetaData",indices = {@Index(value = {"device_id"}
)})

public class MetaData {

    @PrimaryKey(autoGenerate = true)

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private long patientId ;

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    @ColumnInfo(name = "device_id")

    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @ColumnInfo(name = "type")

    private int type;

    public int getType(){
        return this.type;
    }

    public void setType(int type){

        this.type = type;
    }
}

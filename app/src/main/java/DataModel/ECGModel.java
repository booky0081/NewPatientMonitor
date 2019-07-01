package DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import Helper.TimestampConverter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "ECG")
@TypeConverters({TimestampConverter.class,})

public class ECGModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("deviceId")
    @Expose
    @ColumnInfo(name = "meta_id")
    private long metaId;

    @SerializedName("fileId")
    @Expose
    @ColumnInfo(name = "file_id")
    private String fileId;

    @SerializedName("startDate")
    @Expose
    @ColumnInfo(name = "created_date")
    private String started;

    @SerializedName("stopDate")
    @Expose
    @ColumnInfo(name = "stop_date")
    private String ended;

    @SerializedName("type")
    @Expose
    @ColumnInfo(name = "type")
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMetaId() {
        return metaId;
    }

    public void setMetaId(long metaId) {
        this.metaId = metaId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public String getEnded() {
        return ended;
    }

    public void setEnded(String ended) {
        this.ended = ended;
    }

    public void setType(int type){this.type = type;}

    public int getType(){

        return this.type;
    }
}


package DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import Helper.TimestampConverter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "Fluid")
@TypeConverters({TimestampConverter.class,})
public class FluidModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("deviceId")
    @Expose
    @ColumnInfo(name = "meta_id")
    private long metaId;

    @SerializedName("urination")
    @Expose
    @ColumnInfo(name = "urination")
    private String urination = "not set";

    @SerializedName("panWt")
    @Expose
    @ColumnInfo(name = "panWt")
    private String panWt ="not set";

    @SerializedName("FCWater")
    @Expose
    @ColumnInfo(name = "FCWater")
    private String FCWater = "not set";

    @SerializedName("drink")
    @Expose
    @ColumnInfo(name = "drink")
    private String drink = "not set";

    @SerializedName("beforeAdd")
    @Expose
    @ColumnInfo(name = "beforeAdd")
    private  String beforeAdd ="not set";

    @SerializedName("bottleWt")
    @Expose
    @ColumnInfo(name = "bottleWt")
    private String bottleWt = "not set";

    @SerializedName("urineBag")
    @Expose
    @ColumnInfo(name = "urineBag")
    private String urineBag = "not set";


    @SerializedName("beforeEmptyWt")
    @Expose
    @ColumnInfo(name = "beforeEmptyWt")
    private String beforeEmptyWt = "not set";

    @SerializedName("startDate")
    @Expose
    @ColumnInfo(name = "created_date")
    private String started = "not set";

    @SerializedName("stopDate")
    @Expose
    @ColumnInfo(name = "stop_date")
    private String ended = "not set";

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

    public String getUrination() {
        return urination;
    }

    public void setUrination(String urination) {
        this.urination = urination;
    }

    public String getPanWt() {
        return panWt;
    }

    public void setPanWt(String panWt) {
        this.panWt = panWt;
    }

    public String getFCWater() {
        return FCWater;
    }

    public void setFCWater(String FCWater) {
        this.FCWater = FCWater;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getBeforeAdd() {
        return beforeAdd;
    }

    public void setBeforeAdd(String beforeAdd) {
        this.beforeAdd = beforeAdd;
    }

    public String getBottleWt() {
        return bottleWt;
    }

    public void setBottleWt(String bottleWt) {
        this.bottleWt = bottleWt;
    }

    public String getUrineBag() {
        return urineBag;
    }

    public void setUrineBag(String urineBag) {
        this.urineBag = urineBag;
    }

    public String getBeforeEmptyWt() {
        return beforeEmptyWt;
    }

    public void setBeforeEmptyWt(String beforeEmptyWt) {
        this.beforeEmptyWt = beforeEmptyWt;
    }

    public String getStarted(){
        return this.started;
    }

    public void setStarted(String started){
        this.started = started;
    }

    public String getEnded() {
        return ended;
    }

    public void setEnded(String ended) {
        this.ended = ended;
    }
}

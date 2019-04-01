package Dao;

import java.util.Date;
import java.util.List;

import DataModel.DeviceModel;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface DeviceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMetadata(DeviceModel deviceModel);


    @Query("SELECT * FROM DeviceModel ")
    List<DeviceModel> getDevices();


    @Query("UPDATE  DeviceModel set stop_date =  :stopDate WHERE id = :id" )
    void stop(Date stopDate, long id);
}

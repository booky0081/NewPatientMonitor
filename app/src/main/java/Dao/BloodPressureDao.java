package Dao;

import java.util.List;

import DataModel.BloodPressureHistoryModel;
import DataModel.BloodPressureModel;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface BloodPressureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBloodPressure(BloodPressureModel bloodPressureModels);

    @Query("SELECT * FROM BloodPressure ")

    List<BloodPressureModel> getBloodPressureData();

    @Query("SELECT BloodPressure.diastolic ,BloodPressure.systolic,BloodPressure.pulse" +
            ",DeviceModel.created_date, DeviceModel.stop_date,DeviceModel.device_name, " +
            "Patient.first_name, Hospital.hospital_name FROM  BloodPressure JOIN DeviceModel ON DeviceModel.id = BloodPressure.meta_id " +
            " JOIN Patient ON Patient.id = DeviceModel.patient_id JOIN Hospital on Hospital.id = Patient.hospital_id order by DeviceModel.created_date ,DeviceModel.stop_date")

    List<BloodPressureHistoryModel> getHistory();

    @Query("select * from BloodPressure JOIN DeviceModel ON BloodPressure.meta_id  = DeviceModel.id  and date(datetime(DeviceModel.created_date / 1000 , 'unixepoch')) = date('now')")
    List<BloodPressureHistoryModel> getTodayData();

    @Query( "select date('now') ")

    List<String> test();


}

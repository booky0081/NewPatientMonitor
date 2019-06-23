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
            ",  datetime(DeviceModel.created_date / 1000 , 'unixepoch') as  created_date, datetime(DeviceModel.stop_date / 1000 , 'unixepoch') as stop_date,DeviceModel.device_name, " +
            "Patient.first_name, Hospital.hospital_name FROM  BloodPressure JOIN DeviceModel ON DeviceModel.id = BloodPressure.meta_id  and date(datetime(DeviceModel.created_date / 1000 , 'unixepoch')) = :selectedDate " +
            " JOIN Patient ON Patient.id = DeviceModel.patient_id  and Patient.first_name = :patientName JOIN Hospital on Hospital.id = Patient.hospital_id"
            )

    List<BloodPressureHistoryModel> getHistory( String patientName, String selectedDate);

    @Query("select * from BloodPressure JOIN DeviceModel ON BloodPressure.meta_id  = DeviceModel.id  and date(datetime(DeviceModel.created_date / 1000 , 'unixepoch')) = date('now')")
    List<BloodPressureHistoryModel> getTodayData();

    @Query( "select date('now') ")

    List<String> test();


    @Query("select Patient.first_name from BloodPressure JOIN DeviceModel ON BloodPressure.meta_id  = DeviceModel.id  JOIN Patient ON Patient.id = DeviceModel.patient_id group by DeviceModel.patient_id")
    List<String> getPatientList();


    @Query("select date(datetime(DeviceModel.created_date / 1000 , 'unixepoch'))  as created_date from BloodPressure JOIN DeviceModel ON BloodPressure.meta_id  = DeviceModel.id JOIN Patient ON Patient.first_name =:firstName group by  date(datetime(DeviceModel.created_date / 1000 , 'unixepoch')) ")
    List<String> getTimeList(String firstName);

    /*

    @Query("select * from BloodPressure JOIN DeviceModel ON BloodPressure.meta_id  = DeviceModel.id JOIN Patient ON Patient.first_name =:firstName and DeviceModel.created_date =:time ")
    List<String> getDataList(String firstName,String time);
    */



}

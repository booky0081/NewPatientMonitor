package Dao;

import java.util.List;

import DataModel.ECGModel;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ECGDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertECG(ECGModel ecgModel);

    @Query("select Patient.first_name from ECG JOIN DeviceModel ON ECG.meta_id = DeviceModel.id JOIN Patient ON Patient.id = DeviceModel.patient_id group by Patient.id")
    List<String> getPatientList();

    @Query("select date(datetime(DeviceModel.created_date / 1000 , 'unixepoch'))  as created_date from ECG JOIN DeviceModel ON ECG.meta_id  = DeviceModel.id JOIN Patient ON Patient.first_name =:firstName group by   date(datetime(DeviceModel.created_date / 1000 , 'unixepoch')) ")
    List<String> getTimeList(String firstName);

    @Query("SELECT ECG.type, ECG.id as id, DeviceModel.id as meta_id,file_id, datetime(DeviceModel.created_date / 1000 , 'unixepoch') as  created_date, datetime(DeviceModel.stop_date / 1000 , 'unixepoch') as stop_date,DeviceModel.device_name, " +
            "Patient.first_name, Hospital.hospital_name FROM  ECG JOIN DeviceModel ON DeviceModel.id = ECG.meta_id  and date(datetime(DeviceModel.created_date / 1000 , 'unixepoch')) = :selectedDate " +
            " JOIN Patient ON Patient.id = DeviceModel.patient_id  and Patient.first_name = :patientName JOIN Hospital on Hospital.id = Patient.hospital_id"
    )

    List<ECGModel> getHistory( String patientName, String selectedDate);

    @Query("select * from ECG")
    List<ECGModel> get();


    @Query("update ECG set type = :type where id =:id")
    void updateType(long id,int type);

}

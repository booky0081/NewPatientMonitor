package Dao;

import java.util.List;

import DataModel.FluidModel;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface FluidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFluid(FluidModel fluidModel);

    @Query("SELECT Fluid.id,Fluid.urination, DeviceModel.id as meta_id, " +
            "Fluid.panWt, Fluid.urineBag,Fluid.FCWater,Fluid.drink,Fluid.bottleWt,Fluid.beforeEmptyWt,Fluid.beforeAdd "+
            " ,date('now','-1 days')  as stop_date , date(datetime(DeviceModel.created_date / 1000 , 'unixepoch'))  as created_date  "+
            "FROM Fluid  JOIN DeviceModel ON Fluid.meta_id = DeviceModel.id   and date(datetime(DeviceModel.created_date / 1000 , 'unixepoch'))  >= date('now','-1 days') and DeviceModel.patient_id = :patientId")
    List<FluidModel> getFluidList(long patientId);

    @Query("UPDATE Fluid set urination = :urination where id = :id" )
    void updateUrination(String urination, int id);

    @Query("select * from Fluid " )
    List<FluidModel> get();

    @Query("UPDATE Fluid set panWt = :panWt where id = :id" )
    void updatePanWt(String panWt, int id);

    @Query("UPDATE Fluid set FCWater = :FCWater where id = :id" )
    void updateFCWater(String FCWater, int id);

    @Query("UPDATE Fluid set drink = :drink where id = :id" )
    void updateDrink(String drink, int id);

    @Query("UPDATE Fluid set beforeAdd = :beforeAdd where id = :id" )
    void updateBeforeAdd(String beforeAdd, int id);

    @Query("UPDATE Fluid set bottleWt = :bottleWt where id = :id" )
    void updateBottleWt(String bottleWt, int id);

    @Query("UPDATE Fluid set urineBag = :urineBag where id = :id" )
    void updateUrineBag(String urineBag, int id);

    @Query("UPDATE Fluid set beforeEmptyWt = :beforeEmptyWt where id = :id" )
    void updateBeforeEmptyWt(String beforeEmptyWt, int id);

    @Query("select Patient.first_name from Fluid JOIN DeviceModel ON Fluid.meta_id  = DeviceModel.id  JOIN Patient ON Patient.id = DeviceModel.patient_id group by DeviceModel.patient_id")
    List<String> getPatientList();

    @Query("select date(datetime(DeviceModel.created_date / 1000 , 'unixepoch'))  as created_date from Fluid JOIN DeviceModel ON Fluid.meta_id  = DeviceModel.id JOIN Patient ON Patient.first_name =:firstName group by date(datetime(DeviceModel.created_date / 1000 , 'unixepoch')) ")
    List<String> getTimeList(String firstName);


    @Query("SELECT Fluid.id as id  , DeviceModel.id as meta_id, Fluid.beforeAdd ,Fluid.beforeEmptyWt,Fluid.bottleWt, Fluid.drink,Fluid.FCWater,Fluid.panWt,Fluid.urination,Fluid.urineBag" +

            ",datetime(DeviceModel.created_date / 1000 , 'unixepoch') as  created_date, datetime(DeviceModel.stop_date / 1000 , 'unixepoch') as stop_date,DeviceModel.device_name, " +
            "Patient.first_name, Hospital.hospital_name FROM  Fluid JOIN DeviceModel ON DeviceModel.id = Fluid.meta_id  and date(datetime(DeviceModel.created_date / 1000 , 'unixepoch')) = :selectedDate " +
            " JOIN Patient ON Patient.id = DeviceModel.patient_id  and Patient.first_name = :patientName JOIN Hospital on Hospital.id = Patient.hospital_id"
    )

    List<FluidModel> getHistory( String patientName, String selectedDate);

}


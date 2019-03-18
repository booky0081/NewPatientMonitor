package Dao;

import java.util.List;

import DataModel.HospitalModel;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface HospitalDoa {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHospital(HospitalModel  hospitalModel);

    @Delete
    void delete(HospitalModel hospitalModel);

    @Query("UPDATE Hospital Set hospital_name = :name where id =:id")
    void update(String name,long id);

    @Query("SELECT * FROM Hospital")
    List<HospitalModel> getHospitals();

}

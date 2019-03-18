package Dao;

import java.util.List;

import DataModel.PatientModel;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(PatientModel... patientModels);

   @Delete
   void delete(PatientModel patientModel);

   @Query("SELECT * FROM Patient")
   List<PatientModel> getPatients();

}

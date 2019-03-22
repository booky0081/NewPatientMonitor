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
    long insertUser(PatientModel patientModel);

   @Delete
   void delete(PatientModel patientModel);

   @Query("SELECT * FROM Patient")
   List<PatientModel> getPatients();

   @Query("SELECT * FROM Patient WHERE id = :id")
    PatientModel getPatients(long id);

   @Query("UPDATE Patient SET first_name = coalesce(:firstName,Patient.first_name), " +
           " hospital_id = coalesce(:hospitalId, Patient.hospital_id) WHERE id = :patientId ")
    void Update(String firstName,long hospitalId, long patientId);
}

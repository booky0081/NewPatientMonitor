package Database;

import Dao.HospitalDoa;
import Dao.PatientDao;
import DataModel.DeviceModel;
import DataModel.HospitalModel;
import DataModel.PatientModel;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities =  {HospitalModel.class,PatientModel.class,DeviceModel.class},version = 1 ,exportSchema = false)
public abstract class Database  extends RoomDatabase {

    public abstract HospitalDoa getHospitalDao();

    public abstract PatientDao getPatientDao();
}

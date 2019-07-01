package Database;

import Dao.BloodPressureDao;
import Dao.DeviceDao;
import Dao.ECGDao;
import Dao.FluidDao;
import Dao.HospitalDoa;
import Dao.PatientDao;
import DataModel.BloodPressureModel;
import DataModel.DeviceModel;
import DataModel.ECGModel;
import DataModel.FluidModel;
import DataModel.HospitalModel;
import DataModel.PatientModel;
import Helper.TimestampConverter;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@androidx.room.Database(entities =  {HospitalModel.class,PatientModel.class,DeviceModel.class, BloodPressureModel.class, FluidModel.class, ECGModel.class},version = 7, exportSchema = false)
@TypeConverters({TimestampConverter.class})
public abstract class Database  extends RoomDatabase {

    public abstract HospitalDoa getHospitalDao();

    public abstract PatientDao getPatientDao();

    public abstract BloodPressureDao bloodPressureDao();

    public abstract DeviceDao deviceDao();

    public abstract FluidDao fluidDao();

    public abstract ECGDao ecgDao();
}

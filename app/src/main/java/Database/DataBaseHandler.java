package Database;

import android.content.Context;

import androidx.room.Room;

public class DataBaseHandler {

    private static  DataBaseHandler ourInstance = null;

    private static Database db;

    public synchronized static DataBaseHandler getInstance(Context context) {
        if(ourInstance==null){

            ourInstance = new DataBaseHandler(context);
        }
        return ourInstance;
    }

    public synchronized static DataBaseHandler getInstance() {

        return ourInstance;
    }

    public synchronized  Database getDB(){
        return db;
    }

    private DataBaseHandler(Context context) {

         db =  Room.databaseBuilder(context, Database.class, "PatientOMeterDB").allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

}

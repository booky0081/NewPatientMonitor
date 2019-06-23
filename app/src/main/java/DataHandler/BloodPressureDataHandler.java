package DataHandler;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import BluetoothParser.BloodPressurePaser;
import DataModel.BloodPressureModel;
import Database.DataBaseHandler;
import Dialog.DialogInterface;

public class BloodPressureDataHandler implements DialogInterface {

    private static BloodPressureDataHandler ourInstance = null;

    private  long deviceMetaId  = -1;

    public void setDeviceMetaId(long deviceMetaId) {
        this.deviceMetaId = deviceMetaId;
    }

    private BloodPressurePaser bloodPressurePaser;

    private ArrayList<BloodPressureModel> bloodPressureModelArrayList;

    public static synchronized  BloodPressureDataHandler getInstance(){

            if(ourInstance == null){

                ourInstance = new BloodPressureDataHandler();

            }

            return ourInstance;
    }
    private BloodPressureDataHandler(){

        bloodPressureModelArrayList = new ArrayList<>();

        bloodPressurePaser = new BloodPressurePaser();
    }

    public synchronized  BloodPressureModel getLastest(){

        if(bloodPressureModelArrayList.size()>0) {
            return bloodPressureModelArrayList.get(bloodPressureModelArrayList.size() - 1);
        }

        return null;
    }

    public void Reset(){

        bloodPressureModelArrayList = new ArrayList<>();

    }

    @Override
    public void onMessage(String message) {

        Log.d("BlooedDataHandler","REV");
        bloodPressurePaser.Parse(message);

        BloodPressureModel bloodPressureModel = new BloodPressureModel();

        bloodPressureModel.setPulse(bloodPressurePaser.getPulseRate());

        bloodPressureModel.setMetaId(deviceMetaId);

        bloodPressureModel.setDiastolic(bloodPressurePaser.getDiastolicPressure());

        bloodPressureModel.setSystolic(bloodPressurePaser.getSystolicPressure());

        bloodPressureModelArrayList.add(bloodPressureModel);
        /*
        if(bloodPressurePaser.isEnded()) {

            insertData();
        }
        */

    }

    @Override
    public void onError(String message) {

        this.onDisconnected();
    }


    @Override
    public void onDisconnected() {


        if(!bloodPressurePaser.isEnded()) {
            insertData();

        }

        Reset();

    }

    @Override
    public void onConnected(String device) {

        Reset();

        BloodPressureModel  bloodPressureModel = new BloodPressureModel();

        bloodPressureModel.setDiastolic(0);

        bloodPressureModel.setSystolic(0);

        bloodPressureModel.setPulse(0);

        bloodPressureModel.setMetaId(deviceMetaId);

        bloodPressureModelArrayList.add(bloodPressureModel);


    }

    @Override
    public void onObject(Object object) {

    }

    private void insertData(){

        if(!bloodPressureModelArrayList.isEmpty()){

            DataBaseHandler.getInstance().getDB().bloodPressureDao().insertBloodPressure(this.getLastest());

            DataBaseHandler.getInstance().getDB().deviceDao().stop(new Date(System.currentTimeMillis()), deviceMetaId);

            bloodPressureModelArrayList.clear();
        }

    }

    public ArrayList<BloodPressureModel> getBloodPressureModelArrayList() {

        return bloodPressureModelArrayList;

    }

    public long getCuffPressure(){

        return bloodPressurePaser.getCuffPressure();
    }
}

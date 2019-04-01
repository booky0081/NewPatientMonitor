package DataHandler;

import android.util.Log;

import java.util.List;

import API.Base.APIClientInterface;
import API.BloodPressure.BloodPressureAPIClient;
import API.Device.DeviceAPIClient;
import API.Hospital.HospitalAPIClient;
import API.Patient.PatientAPIClient;
import DataModel.BloodPressureModel;
import DataModel.DeviceModel;
import DataModel.HospitalModel;
import DataModel.PatientModel;
import Database.DataBaseHandler;

public class SyncDataHandler {

    public interface  SyncInterface {

         void onSyncFinished(int id);

         void onSynFailed(int id,String mesage);

         void onCompleted(boolean success);

         void onCanceled();

    }

    private boolean continueOnFilaure = false;

    private boolean isCanceled = false;

    private SyncInterface syncInterface;


    private void syncHospita(){


        if (isCanceled){

            syncInterface.onCanceled();

            this.isCanceled = false;

            return;
        }

        List<HospitalModel> hospitalModelList = DataBaseHandler.getInstance().getDB().getHospitalDao().getHospitals();

        HospitalAPIClient hospitalAPIClient = new HospitalAPIClient();

        hospitalAPIClient.Post(hospitalModelList, new APIClientInterface() {
            @Override
            public void onResponseData(Object object) {

            }

            @Override
            public void onReponse() {

                syncInterface.onSyncFinished(0);

                syncPatient();
            }

            @Override
            public void onError(String message) {

                syncInterface.onSynFailed(0,message);

                Log.e("SyncData", message);

                if(continueOnFilaure){

                    syncPatient();

                }else{

                    syncInterface.onCompleted(false);
                }
            }

            @Override
            public void onDone() {

            }
        });
    }

    private void syncPatient(){

        if(isCanceled){

            syncInterface.onCanceled();

            this.isCanceled = false;

            return;
        }

        List<PatientModel> patientModelList   =DataBaseHandler.getInstance().getDB().getPatientDao().getPatients();

        PatientAPIClient patientAPIClient =  new PatientAPIClient();

        patientAPIClient.Post(patientModelList, new APIClientInterface() {
            @Override
            public void onResponseData(Object object) {

            }

            @Override
            public void onReponse() {

                    syncInterface.onSyncFinished(1);

                    syncDevice();
            }

            @Override
            public void onError(String message) {

                syncInterface.onSynFailed(1,message);

                if(continueOnFilaure){

                    syncDevice();

                }else{

                    syncInterface.onCompleted(false);
                }
            }

            @Override
            public void onDone() {

            }
        });


    }


    private void syncDevice(){

        if(isCanceled){

            syncInterface.onCanceled();

            this.isCanceled = false;

            return;
        }


        List<DeviceModel> deviceModelList = DataBaseHandler.getInstance().getDB().deviceDao().getDevices();

        DeviceAPIClient deviceAPIClient = new DeviceAPIClient();

        deviceAPIClient.Post(deviceModelList, new APIClientInterface() {

            @Override
            public void onResponseData(Object object) {


            }

            @Override
            public void onReponse() {

                syncInterface.onSyncFinished(2);


                syncBloodPressure();

            }

            @Override
            public void onError(String message) {

                syncInterface.onSynFailed(2,message);

                Log.e("Sync device",message);
                if(continueOnFilaure){

                    if(!isCanceled) {
                        syncBloodPressure();
                    }

                }else{

                    syncInterface.onCompleted(false);
                }
            }

            @Override
            public void onDone() {


            }
        });
    }

    private void syncBloodPressure(){

        if(this.isCanceled){

            syncInterface.onCanceled();

            this.isCanceled = false;

            return;
        }
        List<BloodPressureModel> bloodPressureModelList = DataBaseHandler.getInstance().getDB().bloodPressureDao().getBloodPressureData();

        BloodPressureAPIClient bloodPressureAPIClient = new BloodPressureAPIClient();

        bloodPressureAPIClient.Post(bloodPressureModelList, new APIClientInterface() {
            @Override
            public void onResponseData(Object object) {

            }

            @Override
            public void onReponse() {

                    syncInterface.onSyncFinished(3);

                    syncInterface.onCompleted(true);
            }

            @Override
            public void onError(String message) {

                syncInterface.onSynFailed(3,message);
                syncInterface.onCompleted(false);


            }

            @Override
            public void onDone() {


            }
        });
    }

    public void setContinueOnFilaure(boolean continueOnFilaure){

        this.continueOnFilaure= continueOnFilaure;
    }

    public void setSyncInterface(SyncInterface syncInterface){

        this.syncInterface = syncInterface;
    }

    public void Sync(){

        this.syncHospita();
    }

    public void Canceled(){

        this.isCanceled = true;
    }
}

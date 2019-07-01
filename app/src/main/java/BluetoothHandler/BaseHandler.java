package BluetoothHandler;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.sql.Date;

import DataModel.DeviceModel;
import DataModel.HospitalModel;
import DataModel.PatientModel;
import Database.DataBaseHandler;
import Dialog.BluetoothDialog;
import Dialog.DialogInterface;
import Dialog.ProfileManagement;
import Dialog.ProfileManagementInterface;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import mahidoleg.patientmonitoring.R;

public abstract class BaseHandler extends Fragment implements DialogInterface ,ProfileManagementInterface{

    @BindView(R.id.blood_pressure_patient_name)
    TextView patientNameField;

    @BindView(R.id.blood_pressure_bluetooth_name)
     TextView bluetoothNameField;

    @BindView(R.id.blood_pressure_bluetooth_status)
     TextView bluetoothStatusField;

    @BindView(R.id.blood_pressure_hospital_id)
     TextView hospitalNameField;

    @BindView(R.id.blood_pressure_manage_button)
    MaterialButton profileManageButton;

    @BindView(R.id.blood_pressure_connect_button)
    MaterialButton bluetoothConnectButton;

    /*
    @BindView(R.id.blood_pressure_history_button)
    MaterialButton historyButton;
    */

    private ProfileManagement profileManagement = null;

    protected  boolean toCreate = true;

    private ProfileManagementInterface profileManagementInterface;

    protected BluetoothDialog bluetoothDialog;

    protected long selectedPatientId = -1;

    protected long currentDeviceMeta = -1;

   // protected HistoryDialog historyDialog;

    private boolean isRunning = false;


    protected void SetUp(Activity activity){


       // if(profileManagement == null){


            profileManagement = new ProfileManagement(activity);

            profileManagementInterface = this;

            setProfileManageButton();

            setBluetoothConnectButton();

            bluetoothDialog.isConnected();
     //   }

    }



    @Override
    public void onStart() {

        super.onStart();


    }

    @Override
    public void onStop() {

        super.onStop();


    }


    protected void setProfileManageButton() {


        profileManageButton.setOnClickListener(v -> {

            if(profileManagement!=null){

                if(selectedPatientId==-1){

                    profileManagement.Show(profileManagementInterface);

                }else{

                    profileManagement.Show(selectedPatientId,profileManagementInterface);

                }
            }
        });
    }

    protected void setBluetoothConnectButton() {

        if(bluetoothDialog.isConnected()){

            this.bluetoothConnectButton.setEnabled(true);
        }else{

            this.bluetoothConnectButton.setEnabled(false);
        }


        this.bluetoothConnectButton.setOnClickListener(
                v -> {

                    if (!isRunning) {

                        bluetoothDialog.Show();

                    } else {

                        if(bluetoothDialog != null ) {
                            bluetoothDialog.Disconnect();
                        }

                        isRunning = !isRunning;

                        profileManageButton.setEnabled(true);

                        this.Stop();
                    }


                });
    }

    protected  abstract  void Parse(String message);

    @Override
    public void onMessage(String message) {

        Parse(message);
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onDisconnected() {

        bluetoothStatusField.setText("Disconnected");

        bluetoothConnectButton.setText("Connect");

        this.Stop();

        DataBaseHandler.getInstance().getDB().deviceDao().stop(new Date(System.currentTimeMillis()),currentDeviceMeta);

        this.profileManageButton.setEnabled(true);

    }

    protected abstract void Start();

    protected abstract void Stop();

    @Override
    public void onConnected(String deviceName) {

        bluetoothStatusField.setText("Connected");

        bluetoothConnectButton.setText("Disconnect");

        isRunning = true;

        profileManageButton.setEnabled(false);

        bluetoothNameField.setText(deviceName);

        this.profileManageButton.setEnabled(false);

        if(toCreate) {

          createDeviceModel();
        }

        Start();
    }

    protected void createDeviceModel(){

        DeviceModel deviceModel = new DeviceModel();

        deviceModel.setPatientId(selectedPatientId);

        deviceModel.setCreateDate(new Date(System.currentTimeMillis()));

        deviceModel.setDeviceName(bluetoothDialog.getDeviceName());

        currentDeviceMeta = DataBaseHandler.getInstance().getDB().deviceDao().insertMetadata(deviceModel);
    }
    @Override
    public void onComplete(long id) {

        selectedPatientId = id;

        if(id < 0){

            patientNameField.setText("");

            hospitalNameField.setText("");

            bluetoothConnectButton.setEnabled(false);

        }else{


            PatientModel patientModel = DataBaseHandler.getInstance().getDB().getPatientDao().getPatients(id);

            if(patientModel == null){
                return;
            }

            Log.d("Patient Name",patientModel.getFirstName());

            patientNameField.setText(patientModel.getFirstName());

            long hospitalId = patientModel.getHospitalId();

            HospitalModel hospitalModel = DataBaseHandler.getInstance().getDB().getHospitalDao().getHospital(hospitalId);

            if(hospitalModel!=null){

                hospitalNameField.setText(hospitalModel.getHosipitalName());
            }

            if(profileManageButton!=null){

                bluetoothConnectButton.setEnabled(true);
            }
        }
    }




    public void setBluetoothDialog(BluetoothDialog bluetoothDialog){

        this.bluetoothDialog = bluetoothDialog;

        bluetoothDialog.setDialogInterface(this);
    }

}

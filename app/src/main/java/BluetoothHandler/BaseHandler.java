package BluetoothHandler;

import android.app.Activity;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import DataModel.HospitalModel;
import DataModel.PatientModel;
import Database.DataBaseHandler;
import Dialog.BluetoothDialog;
import Dialog.DialogInterface;
import Dialog.ProfileManagement;
import Dialog.ProfileManagementInterface;

public abstract class BaseHandler implements DialogInterface ,ProfileManagementInterface{

    private TextView patientNameField;

    private TextView bluetoothNameField;

    private TextView bluetoothStatusField;

    private TextView hospitalNameField;

    private MaterialButton profileManageButton;

    private MaterialButton bluetoothConnectButton;

    private ProfileManagement profileManagement;

    private ProfileManagementInterface profileManagementInterface;

    private BluetoothDialog bluetoothDialog;

    private long selectedPatientId = -1;

    public BaseHandler(Activity actvity){

        bluetoothDialog = new BluetoothDialog(actvity,this);

        profileManagement = new ProfileManagement(actvity);

        profileManagementInterface = this;

    }

    public void setPatientNameField(TextView patientNameField) {
        this.patientNameField = patientNameField;
    }

    public void setBluetoothNameField(TextView bluetoothNameField) {
        this.bluetoothNameField = bluetoothNameField;
    }

    public void setHospitalNameField(TextView hospitalNameField) {
        this.hospitalNameField = hospitalNameField;
    }

    public void setProfileManageButton(final MaterialButton profileManageButton) {
        this.profileManageButton = profileManageButton;

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

    public void setBluetoothConnectButton(MaterialButton bluetoothConnectButton) {

        this.bluetoothConnectButton = bluetoothConnectButton;

        this.bluetoothConnectButton.setEnabled(false);

        this.bluetoothConnectButton.setOnClickListener(v -> bluetoothDialog.Show());
    }




    protected  abstract  void Parse(String message);
    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onDisconnected() {

        bluetoothStatusField.setText("Disconnected");

    }

    @Override
    public void onConnected(String deviceName) {

        bluetoothStatusField.setText("Connected");

        bluetoothNameField.setText(deviceName);
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

    public void setBluetoothStatusField(TextView bluetoothStatusField) {
        this.bluetoothStatusField = bluetoothStatusField;
    }
}

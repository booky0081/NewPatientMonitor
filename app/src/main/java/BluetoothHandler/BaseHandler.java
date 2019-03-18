package BluetoothHandler;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import DataModel.HospitalModel;
import DataModel.PatientModel;
import Database.DataBaseHandler;
import Dialog.ProfileManagement;
import Dialog.ProfileManagementInterface;

public class BaseHandler {

    private TextView patientNameField;

    private TextView bluetoothNameField;

    private TextView hospitalNameField;

    private MaterialButton profileManageButton;

    private MaterialButton bluetoothConnectButton;

    private ProfileManagement profileManagement;

    private ProfileManagementInterface profileManagementInterface;

    private long selectedPatientId = -1;

    public BaseHandler(){

        profileManagementInterface = new ProfileManagementInterface() {
            @Override
            public void onComplete(long id) {



                if(id < 1){

                    patientNameField.setText("");

                    hospitalNameField.setText("");

                    bluetoothConnectButton.setEnabled(false);

                }else{



                    selectedPatientId = id;

                    PatientModel patientModel =DataBaseHandler.getInstance().getDB().getPatientDao().getPatients(id);

                    patientNameField.setText(patientModel.getFirstName());

                    long hospitalId = patientModel.getHospitalId();

                    HospitalModel hospitalModel = DataBaseHandler.getInstance().getDB().getHospitalDao().getHospital(hospitalId);

                    hospitalNameField.setText(hospitalModel.getHosipitalName());

                    if(profileManageButton!=null){

                        bluetoothConnectButton.setEnabled(true);
                    }
                }
            }
        };
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
        profileManageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(profileManagement!=null){

                    if(selectedPatientId==-1){

                        profileManagement.Show(profileManagementInterface);
                    }else{

                        profileManagement.Show(selectedPatientId,profileManagementInterface);
                    }
                }
            }
        });
    }

    public void setBluetoothConnectButton(MaterialButton bluetoothConnectButton) {

        this.bluetoothConnectButton = bluetoothConnectButton;

        this.bluetoothConnectButton.setEnabled(false);
    }

    public void setProfileManagement(ProfileManagement profileManagement) {

        this.profileManagement = profileManagement;

    }


}

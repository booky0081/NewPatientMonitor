package BluetoothHandler;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

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


    private ProfileManagement profileManagement;

    private ProfileManagementInterface profileManagementInterface;

    private BluetoothDialog bluetoothDialog;

    private long selectedPatientId = -1;

    private boolean isRunning = false;


    protected void SetUp(Activity activity){


        if(profileManagement == null){

            profileManagement = new ProfileManagement(activity);

            profileManagementInterface = this;

            setProfileManageButton();

            setBluetoothConnectButton();
        }


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

        this.bluetoothConnectButton.setEnabled(false);

        this.bluetoothConnectButton.setOnClickListener(
                v -> {

                    if (!isRunning) {

                        if(bluetoothDialog!=null) {
                            bluetoothDialog.Show();
                        }

                    } else {

                        if(bluetoothDialog != null ) {
                            bluetoothDialog.Stop();
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

    }

    protected abstract void Start();

    protected abstract void Stop();
    @Override
    public void onConnected(String deviceName) {

        bluetoothStatusField.setText("Connected");

        isRunning = true;

        profileManageButton.setEnabled(false);

        bluetoothNameField.setText(deviceName);

        Start();
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

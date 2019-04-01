package Dialog;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

import BluetoothParser.BloodPressurePaser;
import DataModel.BloodPressureModel;

public class BloodPressureBluetoothDialog extends BluetoothDialog {

    private BloodPressurePaser bloodPressurePaser;

    public BloodPressureBluetoothDialog(Activity activity) {

        super(activity);

        bloodPressurePaser = new BloodPressurePaser();
    }

    public void onDeviceConnected(BluetoothDevice device){

        super.onDeviceConnected(device);

        if(dialogDataInterface!=null){

            dialogDataInterface.onConnected(device.getName());
        }
    }
    @Override
    public void onMessage(String message){

        if(bloodPressurePaser.Parse(message)) {

            BloodPressureModel bloodPressureModel = new BloodPressureModel();

            bloodPressureModel.setDiastolic(bloodPressurePaser.getDiastolicPressure());

            bloodPressureModel.setPulse(bloodPressurePaser.getPulseRate());

            if(this.dialogDataInterface!=null){

                dialogDataInterface.onObject(bloodPressureModel);
            }

            if(this.dialogInterface!=null){

                dialogInterface.onObject(bloodPressureModel);
            }

        }

    }


    public BloodPressurePaser getBloodPressurePaser() {

        return bloodPressurePaser;
    }
}

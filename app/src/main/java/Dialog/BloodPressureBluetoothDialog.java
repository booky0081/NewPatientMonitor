package Dialog;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

import BluetoothParser.BloodPressurePaser;

public class BloodPressureBluetoothDialog extends BluetoothDialog {

    private BloodPressurePaser bloodPressurePaser;

    public BloodPressureBluetoothDialog(Activity activity) {

        super(activity);

    }

    public void onDeviceConnected(BluetoothDevice device){

        super.onDeviceConnected(device);

        if(dialogDataInterface!=null){

            dialogDataInterface.onConnected(device.getName());
        }
    }

}


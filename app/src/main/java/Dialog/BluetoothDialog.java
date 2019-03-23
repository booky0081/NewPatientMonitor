package Dialog;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.concurrent.CopyOnWriteArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mahidoleg.patientmonitoring.R;
import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.DeviceCallback;
import me.aflak.bluetooth.DiscoveryCallback;

public class BluetoothDialog implements DeviceCallback, DiscoveryCallback {

    @BindView(R.id.state_text)
    TextView stateText;

    @BindView(R.id.device_list)
    ListView  deviceList;

    @BindView(R.id.refresh_button)
    Button refreshButton;

    private Activity activity;

    private Unbinder unbinder;

    private AlertDialog dialog;

    private DeviceListAdapter adapter;

    private static CopyOnWriteArrayList<BluetoothDevice> bluetoothDevices;

    private BluetoothDevice toBeConnected;

    private static CopyOnWriteArrayList<BluetoothDevice> connectedDevices;

    private  Bluetooth bluetooth;

    private boolean closeable = true;


    private Dialog.DialogInterface dialogInterface;

    public  BluetoothDialog(Activity activity){

        this.activity = activity;

        View view = activity.getLayoutInflater().inflate(R.layout.bluetooth_layout, null);

        dialog = new AlertDialog.Builder(activity).setView(view).create();



        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {

                if(closeable){

                    dialog.dismiss();
                }
        });

        unbinder = ButterKnife.bind(view);

        stateText = view.findViewById(R.id.state_text);

        deviceList = view.findViewById(R.id.device_list);

        if(bluetoothDevices==null) {
            bluetoothDevices = new CopyOnWriteArrayList<>();
        }

        if(connectedDevices == null){

            connectedDevices = new CopyOnWriteArrayList<>();
        }

        adapter = new DeviceListAdapter(activity,R.layout.list_view,bluetoothDevices);

        deviceList.setAdapter(adapter);

        deviceList.setOnItemClickListener((parent, view1, position, id) -> {

            if(position< 0 || bluetooth==null){
                return;
            }

            BluetoothDevice device = bluetoothDevices.get(position);

            Enable(false);

            if(bluetooth.getPairedDevices().contains(device)){

                bluetooth.connectToAddress(device.getAddress());



            }else{

                toBeConnected = device;
                bluetooth.pair(device);
                Enable(true);

            }
        });
        refreshButton = view.findViewById(R.id.refresh_button);


        refreshButton.setOnClickListener(v -> Refresh());

        bluetooth = new Bluetooth(activity);

        bluetooth.setDeviceCallback(this);

        bluetooth.setDiscoveryCallback(this);

        bluetooth.setCallbackOnUI(activity);

        dialog.setTitle("Connect to Devices");

        dialog.setCanceledOnTouchOutside(false);

        dialog.setCancelable(false);

    }

    public  void setDialogInterface(DialogInterface dialogInterface){

        this.dialogInterface = dialogInterface;
    }
    private synchronized  static void AddConnectedDevice(BluetoothDevice device){

        if(bluetoothDevices.contains(device)){

            bluetoothDevices.remove(device);
        }
        connectedDevices.add(device);
    }

    private synchronized  static void RemoveConnectedDevice(BluetoothDevice device){

        connectedDevices.remove(device);
    }

    private void Refresh() {


        if (bluetooth != null) {

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);


            } else {

                bluetooth.startScanning();

                bluetoothDevices.clear();

                setText("Scanning");

            }

        }
    }

    private void Enable(boolean enable){

        if(dialog.isShowing()) {

            this.refreshButton.setEnabled(enable);

            this.deviceList.setEnabled(enable);

            closeable = enable;
        }


    }

    public void Show(){

        dialog.show();

        Refresh();
    }

    public void dismiss(){

        dialog.dismiss();
    }

    private void setText(final String txt) {

        stateText.setText(txt);

    }

    public void Start(){

        bluetooth.onStart();

        bluetooth.enable();

    }

    public void Stop(){

        bluetooth.onStop();

        unbinder.unbind();

    }

    @Override
    public void onDeviceConnected(BluetoothDevice device) {


        AddConnectedDevice(device);

        dialogInterface.onConnected(device.getName());

    }

    @Override
    public void onDeviceDisconnected(BluetoothDevice device, String message) {

        RemoveConnectedDevice(device);

        dialogInterface.onDisconnected();

        if(dialog.isShowing()){

            Enable(true);
        }

        //setText("Disconnected from " + device.getName());
    }

    @Override
    public void onMessage(String message) {

        dialogInterface.onMessage(message);
    }

    @Override
    public void onDiscoveryStarted() {

        Enable(false);

    }

    @Override
    public void onDiscoveryFinished() {

        Enable(true);

        setText("scan finished");
    }

    @Override
    public void onDeviceFound(BluetoothDevice device) {


      //  if(!connectedDevices.contains(device)) {


        AddDevices(device);
                //  }
        Log.d("discovered ",device.getAddress());


    }

    private void AddDevices(BluetoothDevice device){

        bluetoothDevices.add(device);

        adapter.notifyDataSetChanged();


    }
    @Override
    public void onDevicePaired(BluetoothDevice device) {

            if(toBeConnected == device){

                bluetooth.connectToAddress(device.getAddress());

                Enable(true);
            }

    }

    @Override
    public void onDeviceUnpaired(BluetoothDevice device) {

            Enable(true);
    }

    @Override
    public void onError(String message) {


            Enable(true);
            setText(message);
            dialogInterface.onError(message);
    }

    @Override
    public void onConnectError(BluetoothDevice device, String message) {

            Enable(true);

       setText("Something went wrong with the connection with " + device.getName());
    }

    /*
    public void setBluetooth(Bluetooth blu){
        this.bluetooth = blu;
    }
    */

}

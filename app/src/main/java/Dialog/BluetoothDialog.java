package Dialog;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.concurrent.CopyOnWriteArrayList;

import androidx.appcompat.app.AlertDialog;
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

    private ArrayAdapter<String> adapter;

    private static CopyOnWriteArrayList<BluetoothDevice> bluetoothDevices;

    private BluetoothDevice toBeConnected;

    private static CopyOnWriteArrayList<BluetoothDevice> connectedDevices;

    private  Bluetooth bluetooth;

    private boolean closeable = true;


    private Dialog.DialogInterface dialogInterface;
    public  BluetoothDialog(Activity activity, Dialog.DialogInterface dialogInterface){

        this.activity = activity;

        View view = activity.getLayoutInflater().inflate(R.layout.bluetooth_layout, null);

        dialog = new AlertDialog.Builder(activity).setView(view).create();

        this.dialogInterface = dialogInterface;
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    if(closeable){

                        dialog.dismiss();
                    }
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

        deviceList.setAdapter(new DeviceListAdapter(activity,R.layout.list_view,bluetoothDevices));

        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

                }
            }
        });
        refreshButton = view.findViewById(R.id.refresh_button);



        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Refresh();

            }
        });

        bluetooth = new Bluetooth(activity);

        Log.d("base Handler",bluetooth.toString());
        dialog.setTitle("Connect to Devices");

        dialog.setCanceledOnTouchOutside(false);

        dialog.setCancelable(false);

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

    private void Refresh(){

        bluetooth.startScanning();

        bluetoothDevices.clear();



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
    }

    @Override
    public void onDeviceFound(BluetoothDevice device) {

        if(!connectedDevices.contains(device)) {
            bluetoothDevices.add(device);

        }

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
            dialogInterface.onError(message);
    }

    @Override
    public void onConnectError(BluetoothDevice device, String message) {

            Enable(true);

       // setText("Something went wrong with the connection with " + device.getName());
    }
}

package Dialog;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import BluetoothParser.BaseParser;
import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mahidoleg.patientmonitoring.R;
import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.DeviceCallback;

public class BluetoothDialog implements DeviceCallback {

    @BindView(R.id.state_text)
    TextView stateText;

    @BindView(R.id.device_list)
    ListView  deviceList;

    @BindView(R.id.refresh_button)
    Button refreshButton;

    private Activity activity;

    private Unbinder unbinder;

    private AlertDialog dialog;

    private Bluetooth pairedBluetooth;

    private ArrayAdapter<String> adapter;

    private List<BluetoothDevice> connectedDevices;

    private List<Bluetooth> connectedBluetooths;

    private BaseParser paser;

    public BluetoothDialog(Activity activity, BaseParser parser){

        this.activity = activity;

        View view = activity.getLayoutInflater().inflate(R.layout.bluetooth_layout, null);

        dialog = new AlertDialog.Builder(activity).setView(view).create();

        unbinder = ButterKnife.bind(view);

        stateText = view.findViewById(R.id.state_text);

        deviceList = view.findViewById(R.id.device_list);

        refreshButton = view.findViewById(R.id.refresh_button);

        this.paser = parser;

    }

    public void Show(){

        dialog.show();
    }

    private void setText(final String txt) {

        stateText.setText(txt);

    }

    private void addDevicesToList(){

         List<String> names = new ArrayList<String>();

        for (BluetoothDevice d : pairedBluetooth.getPairedDevices()){
            if (connectedDevices.contains(d)) {
                names.add(d.getName() + " - Connected");
            }
            else
                names.add(d.getName());
        }

        adapter.clear();

        adapter.addAll(names);

        adapter.notifyDataSetChanged();
    }
    public void Start(){

        for (Bluetooth b : connectedBluetooths){
            b.onStart();
            b.enable();
        }
        addDevicesToList();
    }

    public void Stop(){

        pairedBluetooth.onStop();

        for (Bluetooth b : connectedBluetooths){

            b.onStop();

        }
    }
    @Override
    public void onDeviceConnected(BluetoothDevice device) {

        setText("Connected to " + device.getName());

        connectedDevices.add(device);

        addDevicesToList();
    }

    @Override
    public void onDeviceDisconnected(BluetoothDevice device, String message) {

        connectedDevices.remove(device);

        addDevicesToList();

        //setText("Disconnected from " + device.getName());
    }

    @Override
    public void onMessage(String message) {

        paser.Parse(message);
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onConnectError(BluetoothDevice device, String message) {

       // setText("Something went wrong with the connection with " + device.getName());
    }
}

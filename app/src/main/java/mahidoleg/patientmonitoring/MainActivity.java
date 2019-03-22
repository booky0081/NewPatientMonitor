package mahidoleg.patientmonitoring;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.button.MaterialButton;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;
import java.util.List;

import BluetoothHandler.BloodPressureHandler;
import Database.DataBaseHandler;
//import Dialog.BluetoothDialog;
import Dialog.ProfileManagement;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.DeviceCallback;

public class MainActivity extends AppCompatActivity implements OnBMClickListener {

    /* BOOKS BULLSHIT */

    @BindView(R.id.bmb)
    BoomMenuButton bmb;

    @BindView(R.id.chart)
    LineChart chart;

    @BindView(R.id.main_menu_toolbar)
    Toolbar toolbar;




    @BindView(R.id.blood_pressure_manage_button)
    MaterialButton bloodPressureManageButton;

    @BindView(R.id.blood_pressure_connect_button)

    MaterialButton bloodPressureConnectButton;

    @BindView(R.id.blood_pressure_patient_name)
    TextView bloodPressurePatientNameField;

    @BindView(R.id.blood_pressure_hospital_id)
    TextView bloodPressureHospitalIdField;

    @BindView(R.id.blood_pressure_bluetooth_status)
    TextView bloodPressureBluetoothStatus;

    @BindView(R.id.blood_pressure_bluetooth_name)
    TextView bloodPressureBlueToothName;

    private BloodPressureHandler bloodPressureHandler;

    private Unbinder unbinder;

    private String[] menuButton = {"LOGIN", "DISCLAIMER", "MORE"};

    private static final int LOGININTENT = 1;

    private static boolean loggedIn = false;

    private ProfileManagement profileManagement;

    private BluetoothDialog bluetoothDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_menu);
tansOnCreate();
        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        DataBaseHandler.getInstance(this);

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(R.drawable.ic_lock_black_24dp)
                    .normalText(menuButton[i]).listener(this);

            bmb.addBuilder(builder);
        }

        this.SetUpSamepleChart();

        this.SetUpBloodPressureSection(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (loggedIn) {
            HamButton.Builder builder = (HamButton.Builder) bmb.getBuilder(0);
            builder.normalText("LOGOUT");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onBoomButtonClick(int index) {


        if (index == 0) {

            if (!loggedIn) {

                Login();

            } else {

                if (loggedIn) {

                    loggedIn = false;
                    HamButton.Builder builder = (HamButton.Builder) bmb.getBuilder(0);
                    builder.normalText("LOGIN");
                }
            }

        }
    }

    private void Login() {

        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, LOGININTENT);
    }

    private void SetUpSamepleChart() {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            entries.add(new Entry(i, i));
        }

        LineDataSet dataSet = new LineDataSet(entries, "test");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }

    private void SetUpBloodPressureSection(Activity activity){

        if(bloodPressureHandler == null){

            bloodPressureHandler = new BloodPressureHandler(activity);

            bloodPressureHandler.setPatientNameField(bloodPressurePatientNameField);
            bloodPressureHandler.setHospitalNameField(bloodPressureHospitalIdField);
            bloodPressureHandler.setBluetoothStatusField(bloodPressureBluetoothStatus);
            bloodPressureHandler.setBluetoothNameField(bloodPressurePatientNameField);
            bloodPressureHandler.setBluetoothConnectButton(bloodPressureConnectButton);
            bloodPressureHandler.setProfileManageButton(bloodPressureManageButton);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == LOGININTENT) {

            if (resultCode == RESULT_OK) {

                loggedIn = true;
            }
        }
    }
    /* TAN'S BULLSHIT */
    private Bluetooth pairedBluetooth;
    private ListView deviceList;
    private TextView stateText;
    private TextView cuffPressureText;
    private TextView diastolic;
    private TextView systolic;
    private TextView pulse;
    private Button refreshButton;
    private ArrayAdapter<String> adapter;
    private List<BluetoothDevice> connectedDevices;
    private List<Bluetooth> connectedBluetooths;


    protected void tansOnCreate() {

        pairedBluetooth = new Bluetooth(this);
        connectedDevices = new ArrayList<BluetoothDevice>();
        connectedBluetooths = new ArrayList<Bluetooth>();

        stateText = findViewById(R.id.state_text);
        cuffPressureText = findViewById(R.id.cuff_pressure);
        diastolic = findViewById(R.id.diastolic);
        systolic = findViewById(R.id.systolic);
        pulse = findViewById(R.id.pulse);
    }

    private void setText(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stateText.setText(txt);
            }
        });
    }

    private void addDevicesToList(){
        final List<String> names = new ArrayList<String>();
        for (BluetoothDevice d : pairedBluetooth.getPairedDevices()){
            if (connectedDevices.contains(d)) {
                names.add(d.getName() + " - Connected");
            }
            else
                names.add(d.getName());
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                adapter.addAll(names);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        pairedBluetooth.onStart();
        pairedBluetooth.enable();
        for (Bluetooth b : connectedBluetooths){
            b.onStart();
            b.enable();
        }
        addDevicesToList();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pairedBluetooth.onStop();
        for (Bluetooth b : connectedBluetooths){
            b.onStop();
        }
    }

    class BloodPressureCallback implements DeviceCallback {

        private long count = 0;
        private long systolicPressure = 0;
        private long diastolicPressure = 0;
        private long pulseRate = 0;
        private long cuffPressure = 0;
        private int state;

        public int UNKNOWN = -1;
        public  int BEGIN = 0;
        public  int CUFF_H = 1;
        public  int CUFF_L = 2;
        public  int SYS_H = 3;
        public  int SYS_L = 4;
        public  int DIA_H = 5;
        public  int DIA_L = 6;
        public  int PUL_H = 7;
        public  int PUL_L = 8;
        public  int END = 9;

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
            setText("Disconnected from " + device.getName());
        }

        @Override
        public void onMessage(String message) {

            int tmp = 0;
            for (char x : message.toCharArray()) {
                if (x == '\2') {
                    state = BEGIN;
                } else if (state == BEGIN) {
                    tmp = x;
                    state = CUFF_H;
                } else if (state == CUFF_H) {
                    cuffPressure = (tmp << 8) + x;
                    tmp = 0;
                    state = CUFF_L;
                } else if (state == CUFF_L) {
                    tmp = x;
                    state = SYS_H;
                } else if (state == SYS_H) {
                    systolicPressure = (tmp << 8) + x;
                    tmp = 0;
                    state = SYS_L;
                } else if (state == SYS_L) {
                    tmp = x;
                    state = DIA_H;
                } else if (state == DIA_H) {
                    diastolicPressure = (tmp << 8) + x;
                    tmp = 0;
                    state = DIA_L;
                } else if (state == DIA_L) {
                    tmp = x;
                    state = PUL_H;
                } else if (state == PUL_H) {
                    pulseRate = (tmp << 8) + x;
                    tmp = 0;
                    state = PUL_L;
                } else if (state == PUL_L && x == '\3') {
                    state = END;
                } else if (x == '\r' && state == END) {
                    state = UNKNOWN;
                }
            }
            setBP(systolicPressure,diastolicPressure,pulseRate,cuffPressure);

        }

        @Override
        public void onError(String message) {
        }

        @Override
        public void onConnectError(BluetoothDevice device, String message) {
            setText("Something went wrong with the connection with " + device.getName());
        }

        private void setBP( final long systolicPressure, final long diastolicPressure,final long pulseRate,final long cuffPressure ){

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    systolic.setText(String.valueOf(systolicPressure));
                    diastolic.setText(String.valueOf(diastolicPressure));
                    pulse.setText(String.valueOf(pulseRate));
                    cuffPressureText.setText(String.valueOf(cuffPressure));
                }
            });
        }
    }

    public class BluetoothDialog {

        @BindView(R.id.state_text)
        TextView stateText;

        @BindView(R.id.device_list)
        ListView  deviceList;

        @BindView(R.id.refresh_button)
        Button refreshButton;

        private Activity activity;

        private Unbinder unbinder;

        private AlertDialog dialog;

        public BluetoothDialog(Activity activity){

            this.activity = activity;

            View view = activity.getLayoutInflater().inflate(R.layout.bluetooth_layout, null);

            dialog = new AlertDialog.Builder(activity).setView(view).create();

            unbinder = ButterKnife.bind(view);

            stateText = view.findViewById(R.id.state_text);

            deviceList = view.findViewById(R.id.device_list);

            refreshButton = view.findViewById(R.id.refresh_button);

            //
            //deviceList = (ListView) findViewById(R.id.device_list);
            //stateText = (TextView) findViewById(R.id.state_text);
            //refreshButton = (Button) findViewById(R.id.refresh_button);

            adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, new ArrayList<String>());
            deviceList.setAdapter(adapter);
            deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BluetoothDevice device = pairedBluetooth.getPairedDevices().get(position);
                    setText("Connecting to " + device.getName());
                    Bluetooth bluetooth = new Bluetooth(MainActivity.this);
                    bluetooth.onStart();
                    bluetooth.enable();
                    if (device.getName().equals("CA-NIBP-ab53")) {
                        bluetooth.setDeviceCallback(new BloodPressureCallback());
                    }
                    bluetooth.connectToDevice(device);
                    connectedBluetooths.add(bluetooth);
                }
            });

            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDevicesToList();
                    setText("Click on a device to connect to it");
                }
            });

        }

        public void Show(){

            dialog.show();
        }
    }

}

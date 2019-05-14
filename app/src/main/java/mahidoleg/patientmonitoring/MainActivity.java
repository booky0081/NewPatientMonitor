package mahidoleg.patientmonitoring;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.List;

import API.Base.APIClientInterface;
import API.Patient.PatientAPIClient;
import BluetoothHandler.BloodPressureHandler;
import BluetoothHandler.FluidHandler;
import DataHandler.BloodPressureDataHandler;
import DataHandler.FluidDataHandler;
import DataHandler.SyncDataHandler;
import DataModel.PatientModel;
import Database.DataBaseHandler;
import Dialog.BloodPressureBluetoothDialog;
import Dialog.BluetoothDialog;
import History.History;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

//import Dialog.BluetoothDialog;

public class MainActivity extends AppCompatActivity implements OnBMClickListener {

    /* BOOKS BULLSHIT */

    @BindView(R.id.bmb)
    BoomMenuButton bmb;

    @BindView(R.id.main_menu_toolbar)
    Toolbar toolbar;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private BloodPressureHandler bloodPressureHandler;

    private BluetoothDialog bloodPressureBluetooth;

    private BluetoothDialog fluidBluetooth;

    private BloodPressureDataHandler bloodPressureDataHandler;

    private ViewPagerAdapter viewPagerAdapter;

    private FluidHandler fluidHandler;

    private Unbinder unbinder;

    private String[] menuButton = {"LOGIN", "SYNC", "HISTORY"};

    private static final int LOGININTENT = 1;

    private static boolean loggedIn = false;

    private FluidDataHandler fluidDataHandler;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_menu);

        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        DataBaseHandler.getInstance(this);

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(R.drawable.ic_lock_black_24dp)
                    .normalText(menuButton[i]).listener(this);

            bmb.addBuilder(builder);
        }



        activity = this;

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(viewPagerAdapter);

        bloodPressureDataHandler = BloodPressureDataHandler.getInstance();

        bloodPressureBluetooth = new BloodPressureBluetoothDialog(activity);

        bloodPressureBluetooth.setDialogDataInterface(bloodPressureDataHandler);

        fluidBluetooth = new BluetoothDialog(activity);

        fluidDataHandler =  FluidDataHandler.getInstance();

        fluidBluetooth.setDialogDataInterface(fluidDataHandler);


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

        }else if (index == 1 ){

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            SyncDataHandler syncDataHandler = new SyncDataHandler();

            syncDataHandler.setContinueOnFilaure(false);

            syncDataHandler.setSyncInterface(new SyncDataHandler.SyncInterface() {

                @Override
                public void onSyncFinished(int id) {


                    alertDialog.setMessage("Finished Sync " + id);
                }

                @Override
                public void onSynFailed(int id, String mesage) {

                    alertDialog.setMessage("Failed Sync " + id  +" : with " + mesage);
                }

                @Override
                public void onCompleted(boolean success) {

                    if(success) {

                        alertDialog.setMessage("Finished All Sync");

                    }else{

                        alertDialog.setMessage("Not All Data Has Been uploaded");
                    }

                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setText("ClOSE");

                }

                @Override
                public void onCanceled() {

                    alertDialog.dismiss();
                }
            });


            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {

                syncDataHandler.Canceled();

            });

            alertDialog.setCanceledOnTouchOutside(false);

            alertDialog.setCancelable(false);

            alertDialog.setTitle("Syncing Data");

            alertDialog.setMessage("Please Wait");

            alertDialog.show();

            syncDataHandler.Sync();
        }else{

            History();
        }
    }

    private void Login() {

        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, LOGININTENT);
    }

    private void History(){
        Intent historyIntent = new Intent(this, History.class);
        startActivity(historyIntent);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch(position){

                case 0 :

                    bloodPressureHandler =  BloodPressureHandler.newInstance(1,"Blood pressure");

                    bloodPressureHandler.setBluetoothDialog(bloodPressureBluetooth);

                    if(bloodPressureDataHandler.getBloodPressureModelArrayList() != null && !
                            bloodPressureDataHandler.getBloodPressureModelArrayList().isEmpty()){

                        bloodPressureHandler.setData(bloodPressureDataHandler.getBloodPressureModelArrayList());

                    }

                    return bloodPressureHandler;

                case 1:

                    fluidHandler = FluidHandler.newInstance(2,"Fluid");

                    fluidHandler.setBluetoothDialog(fluidBluetooth);


                    return fluidHandler;

            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Blood Pressure";
                case 2:
                    return "EKG";
                case 1:
                    return "Water Pressure";
            }

            return null;
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


    @Override
    protected void onStart() {

        super.onStart();

        bloodPressureBluetooth.Start();

        fluidBluetooth.Start();

        Test();


    }

    @Override
    protected void onStop() {

        super.onStop();

        bloodPressureBluetooth.Stop();

        fluidBluetooth.Stop();
    }

    private void Test(){

        List<PatientModel> patientModelList = DataBaseHandler.getInstance().getDB().getPatientDao().getPatients();

        PatientAPIClient patientAPIClient = new PatientAPIClient();

        patientAPIClient.Post(patientModelList, new APIClientInterface() {

            @Override
            public void onResponseData(Object object) {

            }

            @Override
            public void onReponse() {

            }

            @Override
            public void onError(String message) {

                    Log.d("TEST API", message);
            }

            @Override
            public void onDone() {

            }
        });
    }

    ;
    /*
    /* TAN'S BULLSHIT */

   // private Bluetooth pairedBluetooth;

    /*
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
        ListView deviceList;

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
    */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }


        }
    }

}

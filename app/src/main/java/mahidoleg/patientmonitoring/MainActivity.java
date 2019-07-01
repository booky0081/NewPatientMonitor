package mahidoleg.patientmonitoring;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import BluetoothHandler.BloodPressureHandler;
import BluetoothHandler.ECGHandler;
import BluetoothHandler.FluidHandler;
import DataHandler.BloodPressureDataHandler;
import DataHandler.ECGDataHandler;
import DataHandler.FluidDataHandler;
import DataHandler.SyncDataHandler;
import Database.DataBaseHandler;
import Dialog.BloodPressureBluetoothDialog;
import Dialog.BluetoothDialog;
import history.History;
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

    private ECGHandler ecgHandler;

    private BluetoothDialog  ecgBlueooth;

    private ECGDataHandler ecgDataHandler;

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

        ecgBlueooth = new BluetoothDialog(activity);

        ecgDataHandler  = ECGDataHandler.getInstance();

        ecgBlueooth.setDialogDataInterface(ecgDataHandler);

        ecgDataHandler.context = this;

        ecgBlueooth.setUTF8();
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

            syncDataHandler.setContext(activity);
            syncDataHandler.setSyncInterface(new SyncDataHandler.SyncInterface() {

                @Override
                public void onSyncFinished(int id) {


                    alertDialog.setMessage("Finished Sync " + id);
                }

                @Override
                public void onSynFailed(int id, String mesage) {

                    alertDialog.setMessage("Failed Sync " + id  +" : with " + mesage);
                    Log.d("Sync",mesage);
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

                    return bloodPressureHandler;

                case 1:

                    fluidHandler = FluidHandler.newInstance(2,"Fluid");

                    fluidHandler.setBluetoothDialog(fluidBluetooth);

                    return fluidHandler;

                case 2:
                    ecgHandler = ECGHandler.newInstance(3,"ECG");

                    ecgHandler.setBluetoothDialog(ecgBlueooth);

                    return ecgHandler;
            }

           return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Blood Pressure";
                case 1:
                    return "WaterPressure";
                case 2:
                    return "EKG";
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

        ecgBlueooth.Start();

      //  Test();


    }

    @Override
    protected void onStop() {

        super.onStop();

        bloodPressureBluetooth.Stop();

        fluidBluetooth.Stop();

        ecgBlueooth.Stop();
    }



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

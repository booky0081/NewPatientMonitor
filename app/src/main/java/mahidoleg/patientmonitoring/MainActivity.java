package mahidoleg.patientmonitoring;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity  implements OnBMClickListener{



    private String[] menuButton = {"LOGIN","DISCLAIMER","MORE"};
    private BoomMenuButton bmb;
    private static final int LOGININTENT = 1;
    private static boolean loggedIn  = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_menu);

        bmb =  findViewById(R.id.bmb);

        Toolbar toolbar =  findViewById(R.id.main_menu_toolbar);

        setSupportActionBar(toolbar);


        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(R.drawable.ic_lock_black_24dp)
                    .normalText(menuButton[i]).listener(this);

            bmb.addBuilder(builder);
        }

        FlexboxLayout flexboxLayout = (FlexboxLayout) findViewById(R.id.blood_pressure_layout_details);
        flexboxLayout.setFlexDirection(FlexDirection.ROW);
        LineChart chart = (LineChart) findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<Entry>();
        for(int i= 0 ;i<100;i++){
            entries.add(new Entry(i,i));
        }

        LineDataSet dataSet = new LineDataSet(entries, "test");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
        Switch bloodPressureSwitch =  findViewById(R.id.blood_pressure_device);


        bloodPressureSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
                    arrayAdapter.add("Hardik");
                    arrayAdapter.add("Archit");
                    arrayAdapter.add("Jignesh");
                    arrayAdapter.add("Umang");
                    arrayAdapter.add("Gatti");

                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
                    builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);
                            AlertDialog.Builder builderInner = new AlertDialog.Builder(MainActivity.this);
                            builderInner.setMessage(strName);
                            builderInner.setTitle("Your Selected Item is");
                            builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int which) {
                                    dialog.dismiss();
                                }
                            });
                            builderInner.show();
                        }
                    });
                    builderSingle.show();

                }
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(loggedIn) {
            HamButton.Builder builder = (HamButton.Builder) bmb.getBuilder(0);
            builder.normalText("LOGOUT");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBoomButtonClick(int index) {


        if(index == 0 ){

            if(!loggedIn){

                Login();

            }else{

                if(loggedIn){

                    loggedIn = false;
                    HamButton.Builder builder = (HamButton.Builder) bmb.getBuilder(0);
                    builder.normalText("LOGIN");
                }
            }

        }
    }

    private void Login(){

        Intent loginIntent = new Intent(this,LoginActivity.class);
        startActivityForResult(loginIntent, LOGININTENT);
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
}

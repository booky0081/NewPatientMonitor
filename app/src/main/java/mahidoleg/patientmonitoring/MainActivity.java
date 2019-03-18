package mahidoleg.patientmonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

import Database.DataBaseHandler;
import Dialog.ProfileManagement;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity  implements OnBMClickListener{


    @BindView(R.id.bmb)
    BoomMenuButton bmb;

    @BindView(R.id.chart)
    LineChart chart;

    @BindView(R.id.main_menu_toolbar)
    Toolbar toolbar;

    @BindView(R.id.blood_pressure_manage_button)
    MaterialButton bloodPressureManageButton;

    private Unbinder unbinder;

    private String[] menuButton = {"LOGIN","DISCLAIMER","MORE"};

    private static final int LOGININTENT = 1;

    private static boolean loggedIn  = false;

    private ProfileManagement profileManagement;
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

        this.SetUpSamepleChart();

        this.profileManagement = new ProfileManagement(this);

        bloodPressureManageButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
                profileManagement.Show();
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
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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

    private void SetUpSamepleChart(){
        List<Entry> entries = new ArrayList<>();
        for(int i= 0 ;i<100;i++){
            entries.add(new Entry(i,i));
        }

        LineDataSet dataSet = new LineDataSet(entries, "test");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
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

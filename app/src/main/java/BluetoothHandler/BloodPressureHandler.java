package BluetoothHandler;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import DataHandler.BloodPressureDataHandler;
import DataModel.BloodPressureModel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mahidoleg.patientmonitoring.R;

public class BloodPressureHandler extends BaseHandler {


    @BindView(R.id.diastolic)
    TextView diastolicField;

    @BindView(R.id.systolic)

    TextView systolicField;

    @BindView(R.id.pulse)

    TextView pulseField;

    @BindView(R.id.cuff_pressure)
    TextView cuffPressureField;


    private Unbinder unbinder;

    public synchronized  static BloodPressureHandler newInstance(int page, String title) {

        BloodPressureHandler fragmentFirst = new BloodPressureHandler();

        Bundle args = new Bundle();

        fragmentFirst.setArguments(args);

        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.blood_pressure_layout,container,false);

        unbinder = ButterKnife.bind(this,view);

        Log.d("BloodPressure","Create");

        return view;

    }

    @Override
    public void onDestroyView() {

        Log.d("BloodPressure","Destroy");
        super.onDestroyView();

        unbinder.unbind();
    }

    @Override
    public void onResume() {

        Log.d("BloodPressure","Resume");

        super.onResume();

        SetUp(getActivity());
    }

    @Override
    public void onStart() {

        Log.d("BloodPressure","Start");
        super.onStart();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);



    }



    @Override
    protected void Parse(String message) {

        BloodPressureModel bloodPressureModel = BloodPressureDataHandler.getInstance().getLastest();
       // Log.d("Blood Pressure handler",BloodPressureDataHandler.getInstance().getCuffPressure()+"");
        cuffPressureField.setText(Long.toString(BloodPressureDataHandler.getInstance().getCuffPressure()));
        if(bloodPressureModel!=null){

            setBP(bloodPressureModel);
        }

    }

    @Override
    protected void Start() {

        BloodPressureDataHandler.getInstance().setDeviceMetaId(currentDeviceMeta);

    }

    @Override
    protected void Stop() {

      //  ReloadChart();
    }

    private void setBP(BloodPressureModel bloodPressureModel){


        long diastolic = bloodPressureModel.getDiastolic();

        this.diastolicField.setText(Long.toString(diastolic));

        long systolic = bloodPressureModel.getSystolic();

        this.systolicField.setText(Long.toString(systolic));

        long pulse = bloodPressureModel.getPulse();

        this.pulseField.setText(Long.toString(pulse));

    }


    @Override
    public void onObject(Object object) {

       setBP((BloodPressureModel)object);

    }


    /*
    @Override
    public void setBluetoothDialog(BluetoothDialog bluetoothDialog){

        this.bluetoothDialog = bluetoothDialog;
        bluetoothDialog.setDialogInterface(this);

    }


*/

    public void setData(List<BloodPressureModel> data){


    }

    /*
    private void ReloadChart(){

        List<BloodPressureHistoryModel> bloodPressureModelList = DataBaseHandler.getInstance().getDB().bloodPressureDao().getTodayData();

        if(pulseDataSet==null) {

            List<Entry> pulseDataList = new ArrayList<>();
            pulseDataSet = new LineDataSet(pulseDataList, "Pulse");
            pulseDataSet.setColor(R.color.colorPrimaryDark);
            pulseDataSet.setColor(R.color.colorPrimaryDark);

        }else{

            pulseDataSet.clear();
        }

        if(systolicDataSet==null){

            List<Entry> systolicDataList = new ArrayList<>();
            systolicDataSet = new LineDataSet(systolicDataList,"Systolic");

            systolicDataSet.setColor(R.color.skyBlue);
            systolicDataSet.setCircleColor(R.color.skyBlue);

        }else{

            systolicDataSet.clear();
        }

        if(diastolicDataSet==null){

            List<Entry> diastolicDataList = new ArrayList<>();
            diastolicDataSet = new LineDataSet(diastolicDataList,"Diastolic");
            diastolicDataSet.setColor(R.color.darkBlue);
            diastolicDataSet.setColor(R.color.darkBlue);

        }else{

            diastolicDataSet.clear();
        }

        Log.d("History", bloodPressureModelList.size()+"");

        for(BloodPressureHistoryModel bloodPressureModel : bloodPressureModelList){

            Calendar cal = Calendar.getInstance();

            cal.setTime(bloodPressureModel.getCreateDate());

           // Log.d("CHART ", bloodPressureModel.getDeviceName());

            long hour = cal.get(Calendar.HOUR_OF_DAY);

            long minutes = cal.get(Calendar.MINUTE);

            long seconds = cal.get(Calendar.SECOND);

            minutes = minutes*60;

            hour = hour*60*60;


            long total = minutes+hour+seconds;


            Entry pulseData = new Entry(total,bloodPressureModel.getPulse());

            pulseDataSet.addEntry(pulseData);

            Entry systolicData = new Entry(total,bloodPressureModel.getSystolic());

            systolicDataSet.addEntry((systolicData));

            Entry diastolicData = new Entry(total,bloodPressureModel.getDiastolic());

            diastolicDataSet.addEntry(diastolicData);
        }

        List<ILineDataSet> dataSets = new ArrayList<>();

        dataSets.add(pulseDataSet);
        dataSets.add(diastolicDataSet);
        dataSets.add(systolicDataSet);

        LineData lineData = new LineData(dataSets);

        chart.setData(lineData);

        chart.invalidate();


    }
    */

}

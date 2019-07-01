package History;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Database.DataBaseHandler;
import Dialog.BloodPressureHistoryAdapter;
import Dialog.ECGHistoryAdapter;
import Dialog.FluidHistoryAdapter;
import Dialog.HistoryDialog;
import mahidoleg.patientmonitoring.R;

public class History extends Activity {

    private  int step = 0;

    private String[] values = new String[] { "BLP", "Fluid", "ECG"};

    private ArrayList list ;

    private String selectPatient;

    private String selectedTime;

    private  ArrayAdapter<String> adapter;

    private HistoryDialog bloodPressureHistory;

    private BloodPressureHistoryAdapter bloodPressureHistoryAdapter;

    private HistoryDialog fluidHistory;

    private FluidHistoryAdapter fluidHistoryAdapter;

    private HistoryDialog ECGHisory;

    private ECGHistoryAdapter ecgHistoryAdapter;

    int mode ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        final ListView listview = (ListView) findViewById(R.id.history_device_list);

        list = new ArrayList<String>();

        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);

        }
         adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                stepHandler(item,position);
                step++;
            }

        });

        bloodPressureHistory = new HistoryDialog(this);

        bloodPressureHistoryAdapter = new BloodPressureHistoryAdapter(this,R.layout.blood_pressure_history_layout);

        bloodPressureHistory.setAdapter(bloodPressureHistoryAdapter);

        fluidHistory = new HistoryDialog(this);

        fluidHistoryAdapter = new FluidHistoryAdapter(this,R.layout.water_list);

        fluidHistory.setAdapter(fluidHistoryAdapter);

        ECGHisory = new HistoryDialog(this);

        ecgHistoryAdapter = new ECGHistoryAdapter(this,R.layout.ecg_history);

        ECGHisory.setAdapter(ecgHistoryAdapter);

        ecgHistoryAdapter.setContext(this);
    }


    @Override
    public void onBackPressed() {


     //   Log.d("History","step : "+ step);

        step--;
        switch(step){

            case 0 :

                list.clear();

                for (int i = 0; i < values.length; ++i) {

                    list.add(values[i]);

                }

                selectPatient = null;

                adapter.notifyDataSetChanged();

                break;
            case 1:

                stepHandler(null,0);

                break;

            default: super.onBackPressed(); break;

        }

    }



    private void stepHandler(String item,int position){


        switch(step){

            case 0 :

                List<String> tempList  = new ArrayList<>();

                if(position>-1) {

                    mode = position;
                }

                if(mode == 0 ){

                    tempList = DataBaseHandler.getInstance().getDB().bloodPressureDao().getPatientList();
                }else if  (mode == 1){

                    tempList = DataBaseHandler.getInstance().getDB().fluidDao().getPatientList();

                }else if(mode == 2){

                    tempList = (DataBaseHandler.getInstance().getDB().ecgDao().getPatientList());


                }

        //       Log.d("History",tempList.get(0));


                list.clear();

                list.addAll(tempList);

                adapter.notifyDataSetChanged();

                break;

            case 1:
                if(item!=null) {
                    selectPatient = item;
                }

                switch(mode){

                    case 0:
                        List<String> tempList2 = DataBaseHandler.getInstance().getDB().bloodPressureDao().getTimeList(selectPatient);

                        list.clear();

                        for(int i = 0 ;i<tempList2.size();i++){

                            list.add(tempList2.get(i));
                        }

                        adapter.notifyDataSetChanged();

                        break;
                    case 1 :

                        List<String> tempList3 = DataBaseHandler.getInstance().getDB().fluidDao().getTimeList(selectPatient);

                        list.clear();

                        for(int i = 0 ;i<tempList3.size();i++){

                            list.add(tempList3.get(i));
                        }

                        adapter.notifyDataSetChanged();

                        break;
                    case 2:

                        List<String> tempList4 = DataBaseHandler.getInstance().getDB().ecgDao().getTimeList(selectPatient);
                        list.clear();

                        for(int i = 0 ;i<tempList4.size();i++){

                            list.add(tempList4.get(i));
                        }

                        adapter.notifyDataSetChanged();

                        break;

                    default : break;

                } break;

            case 2:
                step--;
                switch(mode){

                    case 0:
                        if(item!=null) {

                            selectedTime = item;
                       //     Log.d("History",selectedTime + " "+selectPatient);

                            bloodPressureHistory.show();

                            bloodPressureHistoryAdapter.SetData(selectPatient,selectedTime);


                        }
                        break;
                    case 1:

                        if(item!=null) {

                            selectedTime = item;
                     //       Log.d("History",selectedTime + " "+selectPatient);

                            fluidHistory.show();

                            fluidHistoryAdapter.SetData(selectPatient,selectedTime);

                        }
                        break;
                    case 2:

                        if(item!=null) {

                            selectedTime = item;
                       //     Log.d("History",selectedTime + " "+selectPatient);

                            ECGHisory.show();

                            ecgHistoryAdapter.SetData(selectPatient,selectedTime);

                        }
                        break;

                        default: break;

                }

        }
    }




}

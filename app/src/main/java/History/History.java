package History;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Database.DataBaseHandler;
import mahidoleg.patientmonitoring.R;

public class History extends Activity {

    private  int step = 0;

    private String[] values = new String[] { "BLP", "Fluid", "ECG"};

    private ArrayList list ;

    private String selectPatient;

    private String selectedTime;

    private  ArrayAdapter<String> adapter;

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
            }

        });
    }


    @Override
    public void onBackPressed() {


        Log.d("History","step : "+ step);

        switch(step){

            case 1 :

                list.clear();
                for (int i = 0; i < values.length; ++i) {
                    list.add(values[i]);

                }
                step--;
                adapter.notifyDataSetChanged();

                break;
            case 2:

                stepHandler(null,0);
                step=step-2;

                break;

            default: super.onBackPressed(); break;

        }






    }



    private void stepHandler(String item,int position){


        switch(step){

            case 0 :

                List<String> tempList = DataBaseHandler.getInstance().getDB().bloodPressureDao().getPatientList();



                if(position>-1) {

                    mode = position;
                }

                Log.d("History",tempList.get(0));

               // list.addAll(tempList);
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
                        List<Date> tempList2 = DataBaseHandler.getInstance().getDB().bloodPressureDao().getTimeList(item);

                        list.clear();

                        for(int i = 0 ;i<tempList2.size();i++){

                            list.add(tempList2.get(i).toString());

                            Log.d("History",list.get(i).toString());
                        }

                        adapter.notifyDataSetChanged();

                        break;

                    default : break;

                } break;

            case 2:

                if(item!=null) {

                    selectedTime = item;

                }
                break;

        }


        step++;
    }




}

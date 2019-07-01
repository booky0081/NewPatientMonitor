package BluetoothHandler;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import DataHandler.ECGDataHandler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mahidoleg.patientmonitoring.R;

public class ECGHandler extends BaseHandler {

    private final String start ="rdy";

    private final String stop = "end";

    private boolean recording = false;

    private int  x =0;

    @BindView(R.id.start_ecg_record)
    MaterialButton startButton;

    @BindView(R.id.ecg_graph)
    GraphView ecgGraph;

    private LineGraphSeries<DataPoint> mSeries1;

    private Unbinder unbinder;
    public synchronized  static ECGHandler newInstance(int page, String title) {

        ECGHandler fragmentFirst = new ECGHandler();

        Bundle args = new Bundle();

        fragmentFirst.setArguments(args);

        return fragmentFirst;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();

     //   SetUp(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ecg,container,false);

        unbinder = ButterKnife.bind(this,view);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(recording){

                    bluetoothDialog.send(stop);

                    recording = false;

                }else{
                    bluetoothDialog.send(start);

                    recording = true;

                    Log.d("ECG","SEND");

                }



            }
        });

        mSeries1 = new LineGraphSeries<>();

        mSeries1.setDrawDataPoints(true);

        ecgGraph.addSeries(mSeries1);

        return view;
    }

    @Override
    protected void Parse(String message) {

        Log.d("Test","add Data");
        addData();
    }

    @Override
    public void onResume() {
        super.onResume();

        SetUp(getActivity());
    }

    @Override
    protected void Start() {

        x=0;

        ECGDataHandler.getInstance().setDeviceMetaId(currentDeviceMeta);
        Log.d("CurrentDevice",currentDeviceMeta+"");
       resetData();
    }

    @Override
    protected void Stop() {

        ecgGraph.clearSecondScale();

        mSeries1.resetData(new DataPoint[] {
                new DataPoint(0, 0)

        });
        x = 0;
    }

    private void resetData(){

    }

    private void addData(){

       List<Long> data = ECGDataHandler.getInstance().getDatas();


       for(int i = 0;i<data.size();i++){

           mSeries1.appendData(new DataPoint(x,data.get(i) ),false,10);

           x++;
       }

       ECGDataHandler.getInstance().resetData();


    }

    @Override
    public void onObject(Object object) {

    }
}

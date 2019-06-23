package Dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import API.Base.APIClientInterface;
import API.ECG.ECGAPIClient;
import DataModel.ECGModel;
import Database.DataBaseHandler;
import androidx.annotation.NonNull;
import mahidoleg.patientmonitoring.R;

public class ECGHistoryAdapter extends ArrayAdapter<ECGModel> {

    private List<ECGModel> ecgModelList;

    private Context context;

    private Executor executor = Executors.newSingleThreadExecutor();

    public void setContext(Context context) {
        this.context = context;
    }

    public ECGHistoryAdapter(@NonNull Context context, int resource) {
        super(context, resource);

    }

    @Override
    public int getCount(){

        if(ecgModelList == null){
            return 0;
        }
        return ecgModelList.size();
    }

    @Override
    public ECGModel getItem(int position){
        return ecgModelList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public ECGModel getData(int position) {
        return ecgModelList.get(position);
    }

    public void SetData(String patientId , String date){

        this.ecgModelList = DataBaseHandler.getInstance().getDB().ecgDao().getHistory(patientId,date);

        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ecg_history, parent, false);

        }

        TextView startDate = convertView.findViewById(R.id.ecg_history_date);

        TextView endDate = convertView.findViewById(R.id.ecg_history_end_date);

        MaterialButton downloadBtn = convertView.findViewById(R.id.ecg_history_download);

      //  GraphView ecgGraph  = convertView.findViewById(R.id.ecg_history_graph);


        if(ecgModelList!=null && ecgModelList.size()> position){

            ECGModel ecgModel = ecgModelList.get(position);

            startDate.setText(ecgModel.getStarted());

            endDate.setText(ecgModel.getEnded());

            File[] files = context.getFilesDir().listFiles();

         //   Log.d("History","file size"+ files.length);

            for(File file : files){

                if(file.getName().equals((ecgModel.getFileId()))){

                  //  Log.d("Hisory",file.getName());


                    File finalRecord = file;

                    downloadBtn.setOnClickListener(v -> {

                        downloadBtn.setText("Uploading....");

                        executor.execute(() -> {

                            ECGAPIClient ecgapiClient = new ECGAPIClient();

                            ecgapiClient.Upload(finalRecord, new APIClientInterface() {
                                @Override
                                public void onResponseData(Object object) {


                                }

                                @Override
                                public void onReponse() {


                                    ((Activity)context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            downloadBtn.setText("Upload");

                                        }
                                    });

                                }

                                @Override
                                public void onError(String message) {


                                }

                                @Override
                                public void onDone() {

                                }
                            });
                        });
                    });

                    break;
                }
            }

            /*
            if(record!=null) {
                try {


                    BufferedReader br = new BufferedReader(new FileReader(record));

                    String line;
                    int x = 1;
                    ArrayList<DataPoint> valueList = new ArrayList();
                    while ((line = br.readLine()) != null) {


                     //  Log.d("History",line);
                        String[] datas = line.split(",");

                        for (String data : datas) {


                            int value = Integer.parseInt(data.trim());


                            valueList.add(new DataPoint(x, value));

                            x++;

                        }
                    }

                    br.close();

                    DataPoint dataPoints[] = new DataPoint[valueList.size()];

                    for (int i = 0; i < valueList.size(); i++) {

                        dataPoints[i] = valueList.get(i);

                    }
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);

                    ecgGraph.addSeries(series);

                } catch (IOException e) {
                    //You'll need to add proper error handling here
                }


            }else{

                Log.d("History","file not found");
            }

            */
        }

        return convertView;
    }
}

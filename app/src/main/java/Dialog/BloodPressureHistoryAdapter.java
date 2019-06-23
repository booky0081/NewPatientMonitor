package Dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import DataModel.BloodPressureHistoryModel;
import Database.DataBaseHandler;
import androidx.annotation.NonNull;
import mahidoleg.patientmonitoring.R;

public class BloodPressureHistoryAdapter extends ArrayAdapter<BloodPressureHistoryModel>  {

    private List<BloodPressureHistoryModel> bloodPressureHistoryModelList;

    public BloodPressureHistoryAdapter(@NonNull Context context, int resource) {
        super(context, resource);

    }

    @Override
    public int getCount(){

        if(bloodPressureHistoryModelList == null){
            return 0;
        }
        return bloodPressureHistoryModelList.size();
    }

    @Override
    public BloodPressureHistoryModel getItem(int position){
        return bloodPressureHistoryModelList.get(position);
    }


    @Override
    public long getItemId(int position){
        return position;
    }

    public void SetData(String patientName,String selectedDate){

        this.bloodPressureHistoryModelList = DataBaseHandler.getInstance().getDB().bloodPressureDao().getHistory(patientName,selectedDate);

        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.blood_pressure_history_layout, parent, false);

        }

        TextView nameField = convertView.findViewById(R.id.history_patient_field);

        TextView hospitalField = convertView.findViewById(R.id.history_hospital_field);

        TextView deviceNameField = convertView.findViewById(R.id.history_device_name);

        TextView startField = convertView.findViewById(R.id.history_start_field);

        TextView endField = convertView.findViewById(R.id.history_end_field);

        TextView pulseField = convertView.findViewById(R.id.history_pulse);

        TextView systolicField = convertView.findViewById(R.id.history_systolic);

        TextView diastolic = convertView.findViewById(R.id.history_diastolic);

        if(bloodPressureHistoryModelList!=null && bloodPressureHistoryModelList.size()> position){

            BloodPressureHistoryModel bloodPressureHistoryModel = bloodPressureHistoryModelList.get(position);

            nameField.setText(bloodPressureHistoryModel.getFirstName());

            hospitalField.setText(bloodPressureHistoryModel.getHosipitalName());

            deviceNameField.setText(bloodPressureHistoryModel.getDeviceName());

            startField.setText(bloodPressureHistoryModel.getCreateDate().toString());

            endField.setText(bloodPressureHistoryModel.getStopDate().toString());

            pulseField.setText(Long.toString(bloodPressureHistoryModel.getPulse()));

            systolicField.setText(Long.toString(bloodPressureHistoryModel.getSystolic()));

            diastolic.setText(Long.toString(bloodPressureHistoryModel.getDiastolic()));
        }


        return convertView;
    }

}

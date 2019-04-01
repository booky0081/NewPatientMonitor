package Dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import DataModel.DeviceModel;
import DataModel.HospitalModel;
import DataModel.PatientModel;
import Database.DataBaseHandler;
import androidx.annotation.NonNull;
import mahidoleg.patientmonitoring.R;

public class HistoryListAdapter extends ArrayAdapter<DeviceModel> {

    private List<DeviceModel> deviceModelList;

    public HistoryListAdapter(@NonNull Context context, int resource, @NonNull List<DeviceModel> objects) {

        super(context, resource, objects);

        this.deviceModelList = objects;
    }

    @Override
    public int getCount(){
        return deviceModelList.size();
    }

    @Override
    public DeviceModel getItem(int position){
        return deviceModelList.get(position);
    }


    @Override
    public long getItemId(int position){
        return position;
    }

    public void SetData(List<DeviceModel> data){

        this.deviceModelList = data;

        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_layout, parent, false);

        }

        TextView nameField = convertView.findViewById(R.id.history_patient_field);

        TextView hospitalField = convertView.findViewById(R.id.history_hospital_field);


        PatientModel patientModel = DataBaseHandler.getInstance().getDB().getPatientDao().getPatients(deviceModelList.get(position).getId());

        if(patientModel!=null){

            nameField.setText(patientModel.getFirstName());

            HospitalModel hospitalModel = DataBaseHandler.getInstance().getDB().getHospitalDao().getHospital(patientModel.getHospitalId());

            if(hospitalModel!=null){

                hospitalField.setText(hospitalModel.getHosipitalName());
            }

        }

        TextView startField = convertView.findViewById(R.id.history_start_field);

        if(deviceModelList.get(position).getCreateDate()!=null){

            startField.setText(deviceModelList.get(position).getCreateDate().toString());
        }



        return convertView;
    }



}

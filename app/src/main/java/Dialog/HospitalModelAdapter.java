package Dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import DataModel.HospitalModel;
import androidx.annotation.NonNull;

public class HospitalModelAdapter extends ArrayAdapter<HospitalModel> {

    private Context context;

    private List<HospitalModel> hospitalModel;

    public HospitalModelAdapter(@NonNull Context context, int resource, @NonNull List<HospitalModel> objects) {

        super(context, resource, objects);

        this.context = context;

        this.hospitalModel = objects;
    }

    @Override
    public int getCount(){
        return hospitalModel.size();
    }

    @Override
    public HospitalModel getItem(int position){
        return hospitalModel.get(position);
    }


    @Override
    public long getItemId(int position){
        return position;
    }

    public void SetData(List<HospitalModel> data){

        this.hospitalModel = data;

        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (HospitalModel class)
        label.setText(hospitalModel.get(position).getHosipitalName());
        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(hospitalModel.get(position).getHosipitalName());

        return label;
    }
}

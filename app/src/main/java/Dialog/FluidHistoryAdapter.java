package Dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import DataModel.FluidModel;
import Database.DataBaseHandler;
import androidx.annotation.NonNull;
import mahidoleg.patientmonitoring.R;

public class FluidHistoryAdapter extends ArrayAdapter<FluidModel> {

    private List<FluidModel> fluidModelList;

    public FluidHistoryAdapter(@NonNull Context context, int resource) {
        super(context, resource);

    }

    @Override
    public int getCount(){

        if(fluidModelList == null){
            return 0;
        }
        return fluidModelList.size();
    }

    @Override
    public FluidModel getItem(int position){
        return fluidModelList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public FluidModel getData(int position) {
        return fluidModelList.get(position);
    }

    public void SetData(String patientId , String date){

        this.fluidModelList = DataBaseHandler.getInstance().getDB().fluidDao().getHistory(patientId,date);

        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.water_list, parent, false);

        }

        TextView urinationField = convertView.findViewById(R.id.water_list_urination);

        TextView panWTField = convertView.findViewById(R.id.water_list_panWt);

        TextView beforeAddField = convertView.findViewById(R.id.water_list_beforeAdd);

        TextView beforeEmptyWTField = convertView.findViewById(R.id.water_list_beforeEmptyWt);

        TextView bottleWTField = convertView.findViewById(R.id.water_list_bottleWt);

        TextView drinkField = convertView.findViewById(R.id.water_list_drink);

        TextView FCWaterField = convertView.findViewById(R.id.water_list_FCWater);

        TextView urineBagField = convertView.findViewById(R.id.water_list_urineBag);

        CheckBox checkBox = convertView.findViewById(R.id.water_checkeds);

        TextView date = convertView.findViewById(R.id.water_list_date);

        TextView end = convertView.findViewById(R.id.water_list_end_date);


        checkBox.setVisibility(View.INVISIBLE);


        if(fluidModelList!=null && fluidModelList.size()> position){

            FluidModel fluidModel = fluidModelList.get(position);

            urinationField.setText(fluidModel.getUrination());

            panWTField.setText(fluidModel.getPanWt());

            beforeAddField.setText(fluidModel.getBeforeAdd());

            beforeEmptyWTField.setText(fluidModel.getBeforeEmptyWt());

            bottleWTField.setText(fluidModel.getBottleWt());

            drinkField.setText(fluidModel.getDrink());

            FCWaterField.setText(fluidModel.getFCWater());

            urineBagField.setText(fluidModel.getUrineBag());

            date.setText(fluidModel.getStarted());

            end.setText(fluidModel.getEnded());

        }


        return convertView;
    }
}

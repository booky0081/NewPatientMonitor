package BluetoothHandler;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import DataHandler.FluidDataHandler;
import DataModel.FluidModel;
import Database.DataBaseHandler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mahidoleg.patientmonitoring.R;

public class FluidHandler extends BaseHandler {


    @BindView(R.id.addNew)
    MaterialButton addNewButton;

    @BindView(R.id.setPan)
    MaterialButton setPanButton;

    @BindView(R.id.setBottle)
    MaterialButton setBottleButton;

    @BindView(R.id.beforeAdd)
    MaterialButton beforeAddButton;

    @BindView(R.id.adjustTime)
    MaterialButton adjustTimeButton;

    @BindView(R.id.drink)
    MaterialButton drinkButton;

    @BindView(R.id.urineBag)
    MaterialButton urineBagButton;

    @BindView(R.id.waterList)
    ListView listView;


    private Unbinder unbinder;

    private  final String  setPan  = "ีsetPan";

    private final String setBottle = "setBottle";

    private final String beforeAdd = "beforeAdd";

    private  final String adjustTime = "adjustTime";

    private final String drink = "drink.start";

    private final String urineBag = "urinate";


    private WaterListAdapter waterListAdapter;

    private CheckBox currentSelected = null;

    public synchronized  static FluidHandler newInstance(int page, String title) {

        FluidHandler fragmentFirst = new FluidHandler();

        Bundle args = new Bundle();

        fragmentFirst.setArguments(args);

        return fragmentFirst;
    }

    @Override
    public void onStart() {
        super.onStart();

      //  SetUp(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        SetUp(getActivity());
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.water_layout,container,false);

        unbinder = ButterKnife.bind(this,view);

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                createDeviceModel();

                FluidModel fluidModel = new FluidModel();

                fluidModel.setMetaId(currentDeviceMeta);

                DataBaseHandler.getInstance().getDB().fluidDao().insertFluid(fluidModel);

                waterListAdapter.SetData(selectedPatientId);
            }
        });

        setPanButton.setOnClickListener(createCommand(setPan));

        setBottleButton.setOnClickListener(createCommand(setBottle));

        beforeAddButton.setOnClickListener(createCommand(beforeAdd));

        adjustTimeButton.setOnClickListener(createCommand(adjustTime));

        drinkButton.setOnClickListener(createCommand(drink));

        urineBagButton.setOnClickListener(createCommand(urineBag));

        waterListAdapter = new WaterListAdapter(getActivity(),R.layout.water_list);

        listView.setAdapter(waterListAdapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {

           FluidModel fluidModel =  waterListAdapter.getData(position);

           waterListAdapter.setSelected(fluidModel.getId());

           FluidDataHandler.getInstance().setId(fluidModel.getId());

           Log.d("Fluid",fluidModel.getId()+"");

           CheckBox checkBox = view1.findViewById(R.id.water_checkeds);

           if(currentSelected!=null){

               currentSelected.setChecked(false);

           }
           checkBox.setChecked(true);

           currentSelected = checkBox;

        });

        return view;
    }

    private View.OnClickListener createCommand(String command){

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bluetoothDialog.send(command);
            }
        };
    }
    @Override
    protected void Parse(String message) {
            ////un used
        Log.d("fluid",message);
        waterListAdapter.SetData(selectedPatientId);

    }



    @Override
    protected void Start() {

        bluetoothDialog.send("firstConnect");
        waterListAdapter.SetData(selectedPatientId);
     //   fluidDataHandler.setDeviceMetaId(currentDeviceMeta);
    }

    @Override
    protected void Stop() {

    }

    @Override
    public void onObject(Object object) {

    }

    private class WaterListAdapter extends ArrayAdapter<FluidModel> {

        private int selected = -1;

        private List<FluidModel> fluidModelList;


        public WaterListAdapter(@NonNull Context context, int resource) {

            super(context, resource);
        }
        @Override
        public boolean isEnabled(int position)
        {
            return true;
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

        public void SetData(long patientId){

            this.fluidModelList = DataBaseHandler.getInstance().getDB().fluidDao().getFluidList(patientId);

          //  Log.d("Fluid","Set Data" + fluidModelList.size());
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

            CheckBox checkeBox = convertView.findViewById(R.id.water_checkeds);

            TextView startDateField = convertView.findViewById(R.id.water_list_date);
            TextView endDateField =  convertView.findViewById(R.id.water_list_end_date);

            if(fluidModelList!=null){

                FluidModel fluidModel = fluidModelList.get(position);

                urinationField.setText(fluidModel.getUrination());

                panWTField.setText(fluidModel.getPanWt());

                beforeAddField.setText(fluidModel.getBeforeAdd());

                beforeEmptyWTField.setText(fluidModel.getBeforeEmptyWt());

                bottleWTField.setText(fluidModel.getBottleWt());

                drinkField.setText(fluidModel.getDrink());

                FCWaterField.setText(fluidModel.getFCWater());

                urineBagField.setText(fluidModel.getUrineBag());

                startDateField.setText(fluidModel.getStarted());

                endDateField.setText(fluidModel.getEnded());

                if(selected > -1 && fluidModel.getId() == selected){

                    checkeBox.setChecked(true);
                }

            }


            return convertView;
        }

        public void setSelected(int id){
            this.selected  = id;
        }
    }
}

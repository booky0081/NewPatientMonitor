package Dialog;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import DataModel.HospitalModel;
import DataModel.PatientModel;
import Database.DataBaseHandler;
import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mahidoleg.patientmonitoring.R;

public class ProfileManagement {

    @BindView(R.id.manage_profile_patient_name_text_field)
    EditText patienNameField;


    @BindView(R.id.manage_profile_patient_name_spinner)
    Spinner patienSpinner;


    @BindView(R.id.manage_profile_hospital_name_text_field)
    EditText hospitalNameField;


    @BindView(R.id.manage_profile_hospital_name_spinner)
    Spinner hospitalSpinner;


    @BindView(R.id.manage_profile_hospital_name_save_button)
    MaterialButton hospitalNameSaveButton;

    private Unbinder unbinder;

    private  AlertDialog dialog;

    private Activity activity;

    private List<HospitalModel>  hospitalList;

    private List<PatientModel> patienList;

    private List<String>  hospitalStringList;

    private long selectedHospitalId = -1;

    private int selectedPatientId;

    public ProfileManagement(Activity activity){

        View view = activity.getLayoutInflater().inflate(R.layout.manage_profile, null);

        this.activity = activity;

        unbinder = ButterKnife.bind(view);

        dialog = new AlertDialog.Builder(activity).setView(view).create();

        hospitalList= new ArrayList<>();

        hospitalSpinner = view.findViewById(R.id.manage_profile_hospital_name_spinner);

        hospitalNameField = view.findViewById(R.id.manage_profile_hospital_name_text_field);

        hospitalNameSaveButton = view.findViewById(R.id.manage_profile_hospital_name_save_button);

        patienNameField = view.findViewById(R.id.manage_profile_patient_name_text_field);

        patienSpinner = view.findViewById(R.id.manage_profile_patient_name_spinner);

        hospitalList = DataBaseHandler.getInstance().getDB().getHospitalDao().getHospitals();

        HospitalModel tempHospitalModel = new HospitalModel();

        hospitalList.add(0,tempHospitalModel);

        hospitalSpinner.setAdapter(new HospitalModelAdapter(view.getContext(),android.R.layout.simple_spinner_dropdown_item,hospitalList));

        hospitalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){ selectedHospitalId = -1 ; }
                else{ position = position -1; selectedHospitalId = hospitalList.get(position).getId();}

                TextView textView =  (TextView) hospitalSpinner.getSelectedView();

                String name = textView.getText().toString().trim();
                hospitalNameField.setText(name);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        patienSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){ selectedPatientId = -1 ; }
                else{ position = position  -1 ; selectedPatientId = patienList.get(position).getId();}

                TextView textView =  (TextView) patienSpinner.getSelectedView();

                String name = textView.getText().toString().trim();
                patienNameField.setText(name);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        hospitalNameSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                   String newHospitalName = hospitalNameField.getText().toString().trim();

                   if(selectedHospitalId==-1){

                       if(!newHospitalName.isEmpty()) {

                           HospitalModel newHospitalModel = new HospitalModel();
                           newHospitalModel.setHosipitalName(newHospitalName);
                           selectedHospitalId = DataBaseHandler.getInstance().getDB().getHospitalDao().insertHospital(newHospitalModel);

                           ReloadHospitalList();


                           for(int i = 0;i<hospitalList.size();i++){

                               if(hospitalList.get(i).getId() == selectedHospitalId){

                                   hospitalSpinner.setSelection(i);

                                   break;
                               }
                           }

                       }

                   }else{

                       DataBaseHandler.getInstance().getDB().getHospitalDao().update(newHospitalName,selectedHospitalId);
                       ReloadHospitalList();

                   }


            }
        });


    }

    public void ReloadHospitalList(){

        this.hospitalList = DataBaseHandler.getInstance().getDB().getHospitalDao().getHospitals();

        HospitalModel tempHospitalModel = new HospitalModel();

        hospitalList.add(0,tempHospitalModel);

        HospitalModelAdapter hospitalAdapter = (HospitalModelAdapter)hospitalSpinner.getAdapter();

        hospitalAdapter.SetData(hospitalList);

    }
    public void Show(int patientId){



    }

    public  synchronized  void Show(){
        hospitalSpinner.setSelection(-1);
        this.dialog.show();
    }
}

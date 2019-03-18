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

    private long selectedPatientId = -1;

    private  ProfileManagementInterface profileManagementInterface;
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

        this.ReloadHospitalList();

        this.ReloadPatientList();
        patienSpinner.setAdapter(new PatientAdapter(view.getContext(),android.R.layout.simple_spinner_dropdown_item,patienList));

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

                       for(int i =0 ;i<hospitalList.size();i++){

                           if(hospitalList.get(i).getId() == selectedHospitalId
                                    && !newHospitalName.equals(hospitalList.get(i).getId())
                                    && !newHospitalName.isEmpty()){

                               DataBaseHandler.getInstance().getDB().getHospitalDao().update(newHospitalName,selectedHospitalId);
                               ReloadHospitalList();

                           }
                       }


                   }

                   if(selectedPatientId == -1){

                       String newPatientName = patienNameField.getText().toString().trim();

                       if(!newPatientName.isEmpty()){

                           PatientModel newPatientModel = new PatientModel();

                           newPatientModel.setFirstName(newPatientName);

                           newPatientModel.setHospitalId(selectedHospitalId);

                           selectedPatientId = DataBaseHandler.getInstance().getDB().getPatientDao()
                                    .insertUser(newPatientModel);
                       }

                   }

                   if(selectedPatientId > 0){

                       if(profileManagementInterface!=null){

                           profileManagementInterface.onComplete(selectedHospitalId);
                       }

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

    public void ReloadPatientList(){

        this.patienList = DataBaseHandler.getInstance().getDB().getPatientDao().getPatients();

        PatientModel patientModel = new PatientModel();

        patienList.add(0,patientModel);


    }

    public void Show(long patientId){

        ReloadPatientList();

        ReloadHospitalList();

        for(int  i = 0;i<patienList.size();i++){

            if(patienList.get(i).getId()== patientId){

                patienSpinner.setSelection(i+1);

                this.selectedPatientId = patienList.get(i).getId();

                long hospitalId = patienList.get(i).getHospitalId();

                for(int j =0;j< hospitalList.size();j++){

                    if(hospitalList.get(j).getId() == hospitalId){

                        hospitalSpinner.setSelection(j+1);
                    }
                }

                this.selectedHospitalId = patienList.get(i).getId();

            }
        }


    }

    public void Show(ProfileManagementInterface profileManagementInterface){

        ReloadPatientList();

        ReloadHospitalList();

        this.profileManagementInterface = profileManagementInterface;

        patienSpinner.setSelection(-1);

        hospitalSpinner.setSelection(-1);

        this.dialog.show();
    }


    public void Show(long patientId ,ProfileManagementInterface profileManagementInterface){

        ReloadPatientList();

        ReloadHospitalList();

        this.profileManagementInterface = profileManagementInterface;

        for(int  i = 0;i<patienList.size();i++){

            if(patienList.get(i).getId()== patientId){

                patienSpinner.setSelection(i+1);

                this.selectedPatientId = patienList.get(i).getId();

                long hospitalId = patienList.get(i).getHospitalId();

                for(int j =0;j< hospitalList.size();j++){

                    if(hospitalList.get(j).getId() == hospitalId){

                        hospitalSpinner.setSelection(j+1);
                    }
                }

                this.selectedHospitalId = patienList.get(i).getId();

            }
        }

        this.dialog.show();
    }

    public  synchronized  void Show(){

        ReloadPatientList();

        ReloadHospitalList();

        patienSpinner.setSelection(-1);

        hospitalSpinner.setSelection(-1);

        this.dialog.show();
    }
}

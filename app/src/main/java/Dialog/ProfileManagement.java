package Dialog;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

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

        patienList = new ArrayList<>();

        hospitalSpinner = view.findViewById(R.id.manage_profile_hospital_name_spinner);

        hospitalNameField = view.findViewById(R.id.manage_profile_hospital_name_text_field);

        hospitalNameSaveButton = view.findViewById(R.id.manage_profile_hospital_name_save_button);

        patienNameField = view.findViewById(R.id.manage_profile_patient_name_text_field);

        patienSpinner = view.findViewById(R.id.manage_profile_patient_name_spinner);

        hospitalSpinner.setAdapter(new HospitalModelAdapter(view.getContext(),android.R.layout.simple_spinner_dropdown_item,hospitalList));

        hospitalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedHospitalId = hospitalList.get(position).getId();

                hospitalNameField.setText(hospitalList.get(position).getHosipitalName());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.ReloadHospitalList();

        patienSpinner.setAdapter(new PatientAdapter(view.getContext(),android.R.layout.simple_spinner_dropdown_item,patienList));

        patienSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                    selectedPatientId = patienList.get(position).getId();

                    patienNameField.setText(patienList.get(position).getFirstName());

                    long hospitalId = patienList.get(position).getHospitalId();

                    for (int i = 0; i < hospitalList.size(); i++) {

                        if (hospitalList.get(i).getId() == hospitalId) {

                            selectedHospitalId = hospitalId;

                            hospitalSpinner.setSelection(i);

                            break;
                        }

                    }

                }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.ReloadPatientList();

        hospitalNameSaveButton.setOnClickListener(v -> {

            Log.d("selected patientid ",selectedPatientId+"" +selectedHospitalId);

               String hospitalName = hospitalNameField.getText().toString().trim();

               if(selectedHospitalId <1 && !hospitalName.isEmpty() ){

                   CreateHospital(hospitalName);

               }else{

                   int selectedHospitalIndex = hospitalSpinner.getSelectedItemPosition();

                   HospitalModel hospitalModel = hospitalList.get(selectedHospitalIndex);

                   if(hospitalModel.getId()!= selectedHospitalId){

                       return;
                   }

                   if(!hospitalName.isEmpty() && !hospitalModel.getHosipitalName().equals(hospitalName)){

                       UpdateHospital(hospitalName);
                   }
               }


               if(selectedHospitalId< 1){

                   dialog.dismiss();
               }

            String pateintName = patienNameField.getText().toString().trim();

            if(selectedPatientId < 1 && !pateintName.isEmpty()){

                   if(selectedHospitalId>0){

                       CreatePatient(pateintName);
                   }
               }else{


                    int selectePatientIndex = patienSpinner.getSelectedItemPosition();

                    PatientModel patientModel = patienList.get(selectePatientIndex);

                    if(patientModel.getId()!= selectedPatientId){
                        return;
                    }

                    if((!pateintName.isEmpty() && !patientModel.getFirstName().equals(pateintName)

                            ||(selectedHospitalId>0 && selectedHospitalId!= patientModel.getHospitalId()))){

                        UpdatePateint(pateintName);

                    }
               }


               if(selectedPatientId > 0){
                   Log.d("selected patientid ",selectedPatientId+"");
                   profileManagementInterface.onComplete(selectedHospitalId);
               }

               dialog.dismiss();
        });


    }

    public void ReloadHospitalList(){

        this.hospitalList.clear();
        HospitalModel tempHospitalModel = new HospitalModel();

        tempHospitalModel.setHosipitalName("");

        tempHospitalModel.setId(0);

        hospitalList.add(0,tempHospitalModel);

        this.hospitalList.addAll(DataBaseHandler.getInstance().getDB().getHospitalDao().getHospitals());

        /*
        for(int i=0;i<hospitalList.size();i++){

            Log.d("hospital name", hospitalList.get(i).getHosipitalName() + hospitalList.get(i).getId());
        }

*/
        HospitalModelAdapter hospitalAdapter = (HospitalModelAdapter)hospitalSpinner.getAdapter();

        hospitalAdapter.SetData(hospitalList);

        if(selectedHospitalId > 0){

            for(int i=0;i<hospitalList.size();i++){

                if(hospitalList.get(i).getId() == selectedHospitalId){
                     hospitalSpinner.setSelection(i);

                     break;
                }
            }
        }

    }

    public void ReloadPatientList(){

        this.patienList.clear();

        PatientModel patientModel = new PatientModel();

        patientModel.setFirstName("");
        patientModel.setId(0);
        patientModel.setHospitalId(0);
        patienList.add(0,patientModel);

        this.patienList.addAll(DataBaseHandler.getInstance().getDB().getPatientDao().getPatients());

       /* for(int i=0;i<patienList.size();i++){

            Log.d("patient name", patienList.get(i).getFirstName() + patienList.get(i).getId());
        }
*/
        PatientAdapter patientAdapter = (PatientAdapter) patienSpinner.getAdapter();

        patientAdapter.SetData(patienList);
        if(selectedPatientId > 0){

            for(int i=0;i<patienList.size();i++){

                if(patienList.get(i).getId() == selectedPatientId){
                    patienSpinner.setSelection(i);

                    break;
                }
            }
        }



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

                patienSpinner.setSelection(i);

                this.selectedPatientId = patienList.get(i).getId();

                long hospitalId = patienList.get(i).getHospitalId();

                for(int j =0;j< hospitalList.size();j++){

                    if(hospitalList.get(j).getId() == hospitalId){

                        hospitalSpinner.setSelection(j);
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

    private void CreatePatient(String name){

        if(name.isEmpty()|| selectedHospitalId <1){
            return;
        }

        PatientModel patientModel = new PatientModel();

        patientModel.setHospitalId(selectedHospitalId);

        patientModel.setFirstName(name);

        selectedPatientId = DataBaseHandler.getInstance().getDB()
                    .getPatientDao().insertUser(patientModel);

        ReloadPatientList();
    }

    private void UpdatePateint(String name){

        if(name.isEmpty()){
            return;
        }

        DataBaseHandler.getInstance().getDB().getPatientDao().Update(name,selectedHospitalId,selectedPatientId);

        ReloadPatientList();
    }

    private void CreateHospital(String name){

        if(name.isEmpty()){
            return;
        }

        HospitalModel hospitalModel  = new HospitalModel();

        hospitalModel.setHosipitalName(name);

        selectedHospitalId = DataBaseHandler.getInstance().getDB()
                    .getHospitalDao().insertHospital(hospitalModel);
        ReloadHospitalList();
    }

    private void UpdateHospital(String name){

        DataBaseHandler.getInstance().getDB()
                .getHospitalDao().update(name,selectedHospitalId);
        ReloadHospitalList();
    }
}

package Dialog;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mahidoleg.patientmonitoring.R;

public class BluetoothDialog {

    @BindView(R.id.state_text)
    TextView stateText;

    @BindView(R.id.device_list)
    ListView  deviceList;

    @BindView(R.id.refresh_button)
    Button refreshButton;

    private Activity activity;

    private Unbinder unbinder;

    private AlertDialog dialog;

    public BluetoothDialog(Activity activity){

        this.activity = activity;

        View view = activity.getLayoutInflater().inflate(R.layout.bluetooth_layout, null);

        dialog = new AlertDialog.Builder(activity).setView(view).create();

        unbinder = ButterKnife.bind(view);

        stateText = view.findViewById(R.id.state_text);

        deviceList = view.findViewById(R.id.device_list);

        refreshButton = view.findViewById(R.id.refresh_button);

    }

    public void Show(){

        dialog.show();
    }
}

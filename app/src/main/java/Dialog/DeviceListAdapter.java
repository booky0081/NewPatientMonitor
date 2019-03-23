package Dialog;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import mahidoleg.patientmonitoring.R;

public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

    private List<BluetoothDevice> bluetoothDevices;
    public DeviceListAdapter(@NonNull Context context, int resource, @NonNull List<BluetoothDevice> objects) {

        super(context, resource, objects);

        this.bluetoothDevices = objects;

    }

    @Override
    public int getCount(){
        return bluetoothDevices.size();
    }

    @Override
    public BluetoothDevice getItem(int position){
        return bluetoothDevices.get(position);
    }


    @Override
    public long getItemId(int position){
        return position;
    }

    public void SetData(List<BluetoothDevice> data){

        this.bluetoothDevices = data;

        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        if(convertView==null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);

        }
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (HospitalModel class)

        TextView nameField = convertView.findViewById(R.id.bluetooth_name);

        nameField.setText(bluetoothDevices.get(position).getName());

        TextView addressField = convertView.findViewById((R.id.bluetooth_address));

        addressField.setText(bluetoothDevices.get(position).getAddress());


//        bondStateField.setText(bluetoothDevices.get(position).getBondState());
        // And finally return your dynamic (or custom) view for each spinner item

        return convertView;
    }


}

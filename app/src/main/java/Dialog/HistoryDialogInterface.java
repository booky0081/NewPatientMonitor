package Dialog;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import DataModel.DeviceModel;

public interface HistoryDialogInterface {

    void onShow(List<DeviceModel> data);

    void onSelectedDeviceId(int deviceId);

    void onselectedIndex(int index);

    View onBindView(int position , View convertView, ViewGroup parentView);
}

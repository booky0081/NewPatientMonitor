package Dialog;

import android.app.Activity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import mahidoleg.patientmonitoring.R;

public class HistoryDialog {


    private ListView listView;

    private AlertDialog dialog;

    private Activity activity;

    public HistoryDialog(Activity activity){

        this.activity = activity;

        View view = activity.getLayoutInflater().inflate(R.layout.history_selection_layout, null);

        dialog = new androidx.appcompat.app.AlertDialog.Builder(activity).setView(view).create();

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Close", (dialog, which) -> {

                dialog.dismiss();
        });

        listView = view.findViewById(R.id.history_selection_list);

    }

    public void setAdapter(ListAdapter adapter){

        listView.setAdapter(adapter);
    }

    public void show(){

        dialog.show();
    }

    public void dismiss(){

        dialog.dismiss();
    }
}

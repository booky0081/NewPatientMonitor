package BluetoothHandler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mahidoleg.patientmonitoring.R;

public class FluidHandler extends BaseHandler {

    private Unbinder unbinder;

    public synchronized  static FluidHandler newInstance(int page, String title) {

        FluidHandler fragmentFirst = new FluidHandler();

        Bundle args = new Bundle();

        fragmentFirst.setArguments(args);

        return fragmentFirst;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SetUp(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.water_layout,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    protected void Parse(String message) {
            ////un used
    }

    @Override
    protected void Start() {

    }

    @Override
    protected void Stop() {

    }

    @Override
    public void onObject(Object object) {

    }
}

package BluetoothHandler;

import android.app.Activity;

import BluetoothParser.BloodPressurePaser;

public class BloodPressureHandler extends BaseHandler {


    private BloodPressurePaser paser;

    public BloodPressureHandler(Activity activity) {

        super(activity);

        paser = new BloodPressurePaser();

    }

    @Override
    protected void Parse(String message) {

        String result =  paser.Parse(message);
    }


}

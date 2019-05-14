package BluetoothParser;

import android.util.Log;

public class FluidPaser extends BaseParser  {

    private String urineVolume;
    @Override
    public boolean Parse(String data) {

        String weight;
        boolean isData=false;
        if (data.startsWith("urination:")) {
            weight = data.substring("urination:".length());
            float volume = Float.valueOf(weight)-(float)72.0;
            urineVolume = Float.toString(volume);
            Log.e("nigger",urineVolume);
            isData = true;
        }

        return isData;
    }

    public String getUrineVolume() {

        return  urineVolume;
    }
}

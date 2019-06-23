package BluetoothParser;

import android.util.Log;

public class BloodPressurePaser extends BaseParser {


    private long systolicPressure = 0;
    private long diastolicPressure = 0;
    private long pulseRate = 0;
    private int state;
    private long cuffPressure = 0;

    private int UNKNOWN = -1;
    private int BEGIN = 0;
    private int CUFF_H = 1;
    private int CUFF_L = 2;
    private int SYS_H = 3;
    private int SYS_L = 4;
    private int DIA_H = 5;
    private int DIA_L = 6;
    private int PUL_H = 7;
    private int PUL_L = 8;
    private int END = 9;

    private boolean ended = false;

    public long getSystolicPressure() {
        return systolicPressure;
    }

    public long getDiastolicPressure() {
        return diastolicPressure;
    }

    public long getPulseRate() {
        return pulseRate;
    }


    @Override
    public boolean Parse(String message) {

        int tmp = 0;

        boolean isData = false;


        for (char x : message.toCharArray()) {

            if (x == '\2') {

                state = BEGIN;

            } else if (state == BEGIN) {
                tmp = x;
                state = CUFF_H;
            } else if (state == CUFF_H) {
                cuffPressure = (tmp << 8) + x;
                tmp = 0;
                state = CUFF_L;

                isData= true;
            } else if (state == CUFF_L) {
                tmp = x;
                state = SYS_H;
            } else if (state == SYS_H) {
                systolicPressure = (tmp << 8) + x;
                tmp = 0;

                state = SYS_L;

                isData= true;
            } else if (state == SYS_L) {
                tmp = x;
                state = DIA_H;
            } else if (state == DIA_H) {

                diastolicPressure = (tmp << 8) + x;

                tmp = 0;

                state = DIA_L;
                isData= true;
            } else if (state == DIA_L) {
                tmp = x;
                state = PUL_H;
            } else if (state == PUL_H) {

                pulseRate = (tmp << 8) + x;

                tmp = 0;

                state = PUL_L;

                return true;
            } else if (state == PUL_L && x == '\3') {

                state = END;

                ended = true;

            } else if (x == '\r' && state == END) {
                state = UNKNOWN;
            }
        }


        Log.d("BloodPressureParer", cuffPressure+"");
        return isData;
    }

    public long getCuffPressure(){

        return cuffPressure;
    }

    public boolean isEnded(){
        return this.ended;
    }
}

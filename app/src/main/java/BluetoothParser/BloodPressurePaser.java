package BluetoothParser;

public class BloodPressurePaser extends BaseParser {

    private long count = 0;
    private long systolicPressure = 0;
    private long diastolicPressure = 0;
    private long pulseRate = 0;
    private long cuffPressure = 0;
    private int state;

    public int UNKNOWN = -1;
    public  int BEGIN = 0;
    public  int CUFF_H = 1;
    public  int CUFF_L = 2;
    public  int SYS_H = 3;
    public  int SYS_L = 4;
    public  int DIA_H = 5;
    public  int DIA_L = 6;
    public  int PUL_H = 7;
    public  int PUL_L = 8;
    public  int END = 9;

    @Override
    public java.lang.String Parse(java.lang.String message) {


        int tmp = 0;
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
            } else if (state == CUFF_L) {
                tmp = x;
                state = SYS_H;
            } else if (state == SYS_H) {
                systolicPressure = (tmp << 8) + x;
                tmp = 0;
                state = SYS_L;
            } else if (state == SYS_L) {
                tmp = x;
                state = DIA_H;
            } else if (state == DIA_H) {
                diastolicPressure = (tmp << 8) + x;
                tmp = 0;
                state = DIA_L;
            } else if (state == DIA_L) {
                tmp = x;
                state = PUL_H;
            } else if (state == PUL_H) {
                pulseRate = (tmp << 8) + x;
                tmp = 0;
                state = PUL_L;
            } else if (state == PUL_L && x == '\3') {
                state = END;
            } else if (x == '\r' && state == END) {
                state = UNKNOWN;
            }
        }

        return Long.toString(systolicPressure);
    }
}

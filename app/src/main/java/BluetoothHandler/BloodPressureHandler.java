package BluetoothHandler;

import android.app.Activity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import BluetoothParser.BloodPressurePaser;

public class BloodPressureHandler extends BaseHandler {

    private long count = 0;

    private long systolicPressure = 0;

    private long diastolicPressure = 0;

    private long pulseRate = 0;

    private long cuffPressure = 0;

    private long currentElasped;

    private long currentTime;
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

    private TextView systolicField;

    private TextView diastolicField;

    private TextView pulseField;

    private TextView cuffPressureField;

    private LineChart chart;

    private LineDataSet pulseDataSet;

    private LineDataSet systolicDataSet;

    private LineDataSet diastolicDataSet;



    private BloodPressurePaser paser;

    public BloodPressureHandler(Activity activity) {

        super(activity);

        paser = new BloodPressurePaser();

    }

    @Override
    protected void Parse(String message) {


        int tmp = 0;

        for (char x : message.toCharArray()) {

            if (x == '\2') {
                state = BEGIN;
            }
            else if (state == BEGIN) {
                tmp = x;
                state = CUFF_H;
            } else if (state == CUFF_H) {

                cuffPressure = (tmp << 8) + x;

                cuffPressureField.setText(String.valueOf(cuffPressure));

                this.setBP();

                tmp = 0;
                state = CUFF_L;
            } else if (state == CUFF_L) {
                tmp = x;
                state = SYS_H;
            } else if (state == SYS_H) {

                systolicPressure = (tmp << 8) + x;

                systolicField.setText(String.valueOf(systolicPressure));

                this.setBP();

                tmp = 0;
                state = SYS_L;
            } else if (state == SYS_L) {

                tmp = x;

                state = DIA_H;

            } else if (state == DIA_H) {

                diastolicPressure = (tmp << 8) + x;

                diastolicField.setText(String.valueOf(diastolicPressure));

                this.setBP();

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

    }

    @Override
    protected void Start() {

        List<Entry> entries = new ArrayList<>();
        this.diastolicDataSet = new LineDataSet(entries,"disastolic");

        List<Entry> entries2 = new ArrayList<>();
        this.systolicDataSet = new LineDataSet(entries2,"systolicD");

        List<Entry> entries3 = new ArrayList<>();
        this.pulseDataSet = new LineDataSet(entries3,"pulse");

        this.currentTime = System.currentTimeMillis();
        this.currentElasped = 0;
    }

    public void setSystolicField(TextView systolicField) {

        this.systolicField = systolicField;


    }

    public void setDiastolicField(TextView diastolicField) {

        this.diastolicField = diastolicField;

    }

    public void setPulseField(TextView pulseField) {

        this.pulseField = pulseField;

    }

    private void setBP(){


        this.currentElasped = System.currentTimeMillis() - this.currentTime;  

        Entry diastolicData = new Entry(this.diastolicPressure,currentElasped);

        Entry systolicData = new Entry(this.systolicPressure,currentElasped);

        Entry pulseData = new Entry(this.pulseRate,currentElasped);

        pulseDataSet.addEntry(pulseData);

        systolicDataSet.addEntry(systolicData);

        diastolicDataSet.addEntry(diastolicData);

        chart.notifyDataSetChanged();

        chart.invalidate();

    }

    public void setCuffPressureField(TextView cuffPressureField) {

        this.cuffPressureField = cuffPressureField;

    }


    public void setChart(LineChart chart) {
        this.chart = chart;
    }
}

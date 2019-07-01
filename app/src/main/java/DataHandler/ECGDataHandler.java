package DataHandler;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import BluetoothParser.ECGParser;
import DataModel.ECGModel;
import Database.DataBaseHandler;
import Dialog.DialogInterface;
import androidx.annotation.RequiresApi;

public class ECGDataHandler implements DialogInterface {

    private static ECGDataHandler ourInstance = null;

    private ECGParser ecgParser;

    public Context context;

    private File file;

    private FileOutputStream fileOutputStream;

    private CopyOnWriteArrayList<Long> datas;

    private  long deviceMetaId  = -1;

   private boolean bus = false;

   private boolean address = false;

   private boolean product = false;

   private boolean start  = false;

   private int second = 0;

   private int productId = 0;

   private  long data = 0;

   private long Add = 0;

   private long Bus = 0;

   private int type = 0;

   private long ecgId = 0;

   private Executor executor = Executors.newSingleThreadExecutor();



    private int xtest = 0;

    private Random r = new Random();

    public static synchronized ECGDataHandler getInstance(){

        if(ourInstance == null){

            ourInstance = new ECGDataHandler();
        }

        return ourInstance;
    }


    private void ECGDataHandler(){

        ecgParser = new ECGParser();



    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMessage(String message) {

    //    Log.d("ECG Data",message);
        executor.execute(() -> {

            for (char x : message.toCharArray()) {

                Log.d("ECG int",(x)+"");
                Log.d("ECG",((int)x)+"");

                if(x==0x1f){

                    continue;

                }else if(!bus){

                        bus = true;

                        Bus =  ((int)x);

                        Log.d("ECG BUG", Bus+"");
                    }else if(!address){

                        address = true;

                        Add = (int)x;


                        Log.d("ECG Add", Add+"");

                    }else if(!product){

                        productId = (int)x;


                        Log.d("ECG Product", productId+"");
                //        Log.d("ECG META","AD" + Add +" "+ Bus+" "+productId);

                        if(productId == 1 || productId == 2){

                            product = true;

                            if(type==0){

                                type = productId;

                                DataBaseHandler.getInstance().getDB().ecgDao().updateType(ecgId,type);
                            }

                        }



                    }else if(x=='\n' || x=='\3'|| x=='\r'){


                        bus = false;

                        address = false;

                        product = false;

                        data = 0;

                        second = 0;

                    }else {


                            if (second == 0) {

                                data = (x<<16);

                                second++;

                            } else if (second == 1) {

                                data += (x<<8);

                                second ++;
                            } else {

                                data += x;

                                second =0;

                                if(data>0){

                                    datas.add(data);


                                    updateFile(data+"");
                                }


                                data =0;
                            }


                    }
                }






        });


    }



    public void resetData(){
        this.datas.clear();
    }

    @Override
    public void onError(String message) {

        updateData();
    }

    @Override
    public void onDisconnected() {

        type=0;

        bus = false;

        address = false;

        product = false;

        data = 0;

        second = 0;
        updateData();
    }

    @Override
    public void onConnected(String device) {

        Log.d("ECG","Inserted");

        type=0;

        bus = false;

        address = false;

        product = false;

        data = 0;

        second = 0;
        try {

            insertData();



        } catch (FileNotFoundException e) {

            Log.d("Test",e.getMessage());
            e.printStackTrace();
        }


    }

    @Override
    public void onObject(Object object) {

    }

    public void setDeviceMetaId(long deviceMetaId) {

        this.deviceMetaId = deviceMetaId;
    }

    private void insertData() throws FileNotFoundException {

        long currentTime = System.currentTimeMillis();

        String currentTimeString = Long.toString(currentTime);

        file = new File(context.getFilesDir(), currentTimeString);

        fileOutputStream = new FileOutputStream(file,true);

     //   Log.d("ECG",file.getAbsolutePath());

        ECGModel ecgModel = new ECGModel();

     //   Log.d("ECG",deviceMetaId+"");
        ecgModel.setFileId(currentTimeString);

        ecgModel.setMetaId(deviceMetaId);

        ecgId = DataBaseHandler.getInstance().getDB().ecgDao().insertECG(ecgModel);

        Log.d("ECG","nserted"+ecgId +" "+deviceMetaId);

        datas = new CopyOnWriteArrayList<>();


    }

    private synchronized void updateFile(String data){

        try {

            fileOutputStream.write((data + ",").getBytes());


        } catch (IOException e) {


        }


    }

    private void updateData(){


        DataBaseHandler.getInstance().getDB().deviceDao().stop(new Date(System.currentTimeMillis()), deviceMetaId);
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        file = null;

    }

    public CopyOnWriteArrayList<Long> getDatas(){
        return datas;
    }
}

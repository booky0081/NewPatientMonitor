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
import java.util.Hashtable;
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

   private int second = 0;

   private int productId = 0;

   private  long data = 0;

   private Executor executor = Executors.newSingleThreadExecutor();





    Hashtable<String, String> character
            = new Hashtable<String, String>();


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

        character.put("\2","START");

        character.put("\3","END");




    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMessage(String message) {


        executor.execute(() -> {

            for (char x : message.toCharArray()) {



                if(x == 0x1f) {

                    continue;

                }else {

                    if(!bus && x=='\2'){

                        bus = true;

                        Log.d("Bus",((int)x)+"");

                    }else if(!address){

                        address = true;

                        Log.d("Address",((int)x)+"");

                    }else if(!product){

                        productId =( int )x;


                        Log.d("Product",((int)x)+"");

                        product = true;

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

                                datas.add(data);

                                updateFile(data+"");

                                data =0;
                            }


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

        updateData();
    }

    @Override
    public void onConnected(String device) {

      //  Log.d("ECG","Inserted");
        try {

            insertData();



        } catch (FileNotFoundException e) {
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

      //  ecgModel.setMetaId(deviceMetaId);

        Long ecgId = DataBaseHandler.getInstance().getDB().ecgDao().insertECG(ecgModel);



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

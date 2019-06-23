package DataHandler;

import android.util.Log;

import BluetoothParser.FluidPaser;
import DataModel.FluidModel;
import Database.DataBaseHandler;
import Dialog.DialogInterface;

public class FluidDataHandler implements DialogInterface {

    private static FluidDataHandler ourInstance = null;

    private  int currentId = -1;

    private FluidPaser fluidPaser ;

    public static synchronized FluidDataHandler getInstance(){

        if(ourInstance == null){

            ourInstance = new FluidDataHandler();

        }

        return ourInstance;
    }
    private FluidDataHandler(){

        fluidPaser = new FluidPaser();
    }

    public void create(int deviceMetaId){

        FluidModel fluidModel  = new FluidModel();

        fluidModel.setMetaId(deviceMetaId);

        DataBaseHandler.getInstance().getDB().fluidDao().insertFluid(fluidModel);
    }

    public void setId(int id ){
        this.currentId = id;
    }
    @Override
    public void onMessage(String message) {

        fluidPaser.Parse(message);

        Log.d("Fluid",message);
        if(currentId < 1){

            return;

        }
        switch (fluidPaser.getType()){

            case 1 :

                DataBaseHandler.getInstance().getDB().fluidDao().updateUrination(fluidPaser.getWeight(),currentId);
                break;

            case 2:
                DataBaseHandler.getInstance().getDB().fluidDao().updatePanWt(fluidPaser.getWeight(),currentId);
                break;
            case 3:
                DataBaseHandler.getInstance().getDB().fluidDao().updateFCWater(fluidPaser.getWeight(),currentId);
                break;
            case 4:
                DataBaseHandler.getInstance().getDB().fluidDao().updateDrink(fluidPaser.getWeight(),currentId);
                break;
            case 5:
                DataBaseHandler.getInstance().getDB().fluidDao().updateBeforeAdd(fluidPaser.getWeight(),currentId);
                break;
            case 6:
                DataBaseHandler.getInstance().getDB().fluidDao().updateBottleWt(fluidPaser.getWeight(),currentId);
                break;
            case 7:
                DataBaseHandler.getInstance().getDB().fluidDao().updateUrineBag(fluidPaser.getWeight(),currentId);
                break;
            case 8:
                DataBaseHandler.getInstance().getDB().fluidDao().updateBeforeEmptyWt(fluidPaser.getWeight(),currentId);
                break;
                default: break;
        }
    }

    @Override
    public void onError(String message) {

        this.onDisconnected();
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnected(String device) {


    }

    @Override
    public void onObject(Object object) {

    }


}

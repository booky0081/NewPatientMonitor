package DataHandler;

import BluetoothParser.FluidPaser;
import Dialog.DialogInterface;

public class FluidDataHandler implements DialogInterface {

    private static FluidDataHandler ourInstance = null;

    private  long deviceMetaId  = -1;

    public void setDeviceMetaId(long deviceMetaId) {
        this.deviceMetaId = deviceMetaId;
    }

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

    @Override
    public void onMessage(String message) {
        fluidPaser.Parse(message);
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

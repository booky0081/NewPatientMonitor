package Dialog;

public interface DialogInterface {

     void onMessage (String message);

     void onError(String message);

     void onDisconnected();

     void onConnected(String device);

}

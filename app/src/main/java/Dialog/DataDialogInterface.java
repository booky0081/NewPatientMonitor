package Dialog;

public interface DataDialogInterface {

     void onMessage(String message);

     void onObject(Object object);

     void onError(String message);

     void onDisconnected(String message);
}

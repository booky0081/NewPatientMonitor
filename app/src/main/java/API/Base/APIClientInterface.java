package API.Base;

public interface APIClientInterface {

    void onResponseData(Object object);

    void onReponse();

    void onError(String message);

    void onDone();
}

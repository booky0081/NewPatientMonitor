package BluetoothParser;

public class FluidPaser extends BaseParser  {

    private String data;
    @Override
    public boolean Parse(String data) {

        this.data = data;
        return false;
    }

    public String getData() {

        return  data;
    }
}

package BluetoothParser;

public class FluidPaser extends BaseParser  {

    private String weight;

    private int type = 0;
    @Override
    public boolean Parse(String message) {



        if (message.startsWith("urination:")) {
            type = 1;
            weight = message.substring("urination:".length());

        }
        else if (message.startsWith("panWt:")) {
            type = 2;
            weight = message.substring("panWt:".length());

        }
        else if (message.startsWith("FCWater:")) {

            type = 3;
            weight = message.substring("FCWater:".length());

        }
        else if (message.startsWith("drink:")) {
            type = 4;
            weight = message.substring("drink:".length());

        }
        else if (message.startsWith("beforeAdd:")) {
            type = 5;
            weight = message.substring("beforeAdd:".length());

        }
        else if (message.startsWith("bottleWt:")) {
            type = 6;
            weight = message.substring("bottleWt:".length());

        }
        else if (message.startsWith("urineBag:")) {
            type = 7;
            weight = message.substring("urineBag:".length());

        }
        else if (message.startsWith("beforeEmptyWt:")) {
            type =7;
            weight = message.substring("beforeEmptyWt:".length());

        }

        return true;

    }

    public int getType(){
        return this.type;
    }

    public String getWeight(){
        return this.weight;
    }


}

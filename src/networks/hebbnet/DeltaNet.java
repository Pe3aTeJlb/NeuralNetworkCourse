package networks.hebbnet;

import networks.Network;

public class DeltaNet extends Network {

    public DeltaNet(int[] inputNeuronsCount) {
        super(inputNeuronsCount);
    }

    @Override
    public void train(String path) {

    }

    @Override
    public void simulate(String input) {

    }

    @Override
    public String toString(){
        return "Delta rule";
    }

}

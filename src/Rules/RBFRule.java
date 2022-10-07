package Rules;

import network.Network;

public class RBFRule extends Rule{

    @Override
    public void setNetwork(Network network) {
        this.net = network;
    }

    @Override
    public void train(String dataset) {

    }

    @Override
    public String toString() {
        return "RBF";
    }

}

package rules;

import network.Network;

public abstract class Rule {

    public abstract void setNetwork(Network network);

    public abstract Network getNetwork();

    public abstract void train(String dataset);

    @Override
    public abstract String toString();

}
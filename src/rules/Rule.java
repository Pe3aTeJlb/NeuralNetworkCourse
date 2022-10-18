package rules;

import network.Network;

public abstract class Rule {

    public abstract void createNetwork(int[] inputNeuronsCount);

    public abstract void setNetwork(Network network);

    public abstract Network getNetwork();

    public abstract void train(String dataset);

    public abstract String getRegex();
    public abstract String getPrompt();

    @Override
    public abstract String toString();

}
package rules;

import network.Network;
import network.SimpleNetwork;

import java.io.InputStream;
import java.util.Scanner;

public class DeltaRule extends Rule {

    private SimpleNetwork net;

    private double delta;
    private double learnSpeed = 5;

    public DeltaRule(int[] inputNeuronsCount){
        net = new SimpleNetwork(inputNeuronsCount);
        net.setRule(this);
    }

    @Override
    public void setNetwork(Network network) {
        net = (SimpleNetwork) network;
        net.setRule(this);
    }

    @Override
    public Network getNetwork() {
        return net;
    }

    @Override
    public void train(String dataset) {

        net.reset();

        InputStream inputStream = HebbRule.class.getResourceAsStream(dataset);
        Scanner reader = new Scanner(inputStream);
        while (reader.hasNextLine()) {

            String line = reader.nextLine();
            if(line.equals("")) continue;

            String[] data =  line.trim().split(" ");
            System.out.println("input: " + line);

            delta = Integer.parseInt(data[2]) - net.simulate(data);

            for(int i = 0; i < net.neurons[0].length; i++){
                net.neurons[0][i].updateWeight(delta * Integer.parseInt(data[i]) * learnSpeed);
            }

            net.updateBias(Integer.parseInt(data[2]));

        }

    }

    @Override
    public String toString(){
        return "Delta rule";
    }

}

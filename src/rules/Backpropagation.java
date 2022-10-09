package rules;

import network.BackprogNetwork;
import network.Network;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Backpropagation extends Rule{

    private BackprogNetwork net;

    private double delta;
    private double learnSpeed = 5;
    private int error;

    public Backpropagation(int[] inputNeuronsCount){
        net = new BackprogNetwork(inputNeuronsCount);
        net.setRule(this);
    }

    @Override
    public void setNetwork(Network network) {
        net = (BackprogNetwork) network;
        net.setRule(this);
    }

    @Override
    public Network getNetwork() {
        return null;
    }

    @Override
    public void train(String dataset) {

        net.reset();

        InputStream inputStream = HebbRule.class.getResourceAsStream(dataset);
        Scanner reader = new Scanner(inputStream);
        ArrayList<String> lines = new ArrayList<>();
        while (reader.hasNextLine()){
            lines.add(reader.nextLine());
        }

        do {
            error = 0;
            for(String line: lines) {
                if (line.equals("")) continue;

                String[] data = line.trim().split(" ");
                System.out.println("input: " + line);

                delta = Integer.parseInt(data[2]) - net.simulate(data);
                error += delta;

                for (int i = 0; i < net.neurons[0].length; i++) {
                    net.neurons[0][i].updateWeight(delta * Integer.parseInt(data[i]) * learnSpeed);
                }

                net.updateBias(Integer.parseInt(data[2]));
            }
        } while (error != 0);

    }

    @Override
    public String toString() {
        return "BackPropagation";
    }

}

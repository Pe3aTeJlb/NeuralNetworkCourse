package rules;

import network.Network;
import network.RBFNetwork;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class RBFNRule extends Rule{

    private String regex = "^([1-9][0-9]*+(\\s)?){3}+$";
    private String prompt = "example: 2 2 1";

    private RBFNetwork net;

    @Override
    public void createNetwork(int[] inputNeuronsCount) {
        this.net = new RBFNetwork(inputNeuronsCount);
        net.setRule(this);
    }

    @Override
    public void setNetwork(Network network) {
        net = (RBFNetwork) network;
        net.setRule(this);
    }

    @Override
    public Network getNetwork() {
        return net;
    }

    public void train(String dataset) {

        net.reset();

        ArrayList<double[]> inputs = new ArrayList<>();
        ArrayList<double[]> outputs = new ArrayList<>();
        ArrayList<double[]> centers = new ArrayList<>();

        InputStream inputStream = HebbRule.class.getResourceAsStream(dataset);
        Scanner reader = new Scanner(inputStream);
        while (reader.hasNextLine()) {

            String line = reader.nextLine();
            if(line.equals("")) continue;

            //input input to double vector
            String[] buff =  line.trim().split(" ");
            double[] data = new double[buff.length];
            for(int i = 0; i < buff.length; i++){
                data[i] = Double.parseDouble(buff[i]);
            }

            inputs.add(Arrays.copyOfRange(data, 0, data.length - net.outputSize));
            outputs.add(Arrays.copyOfRange(data, data.length - net.outputSize, data.length));
            if(data[data.length-1] == 1){
                centers.add(Arrays.copyOfRange(data, 0, data.length - net.outputSize));
            }

        }

        //set centers
        for(int i = 0; i < net.inputSize; i++){
            if(i != centers.size()) {
                net.neurons[0][i].setCenter(centers.get(i));
            } else {
                net.neurons[0][i].setCenter(centers.get(centers.size()-1));
            }
        }

        for (int cnt = 0; cnt < 1000; cnt++) {

            //Train network
            int pointer = ThreadLocalRandom.current().nextInt(0, inputs.size());
            double[] data = inputs.get(pointer);

            //Simulate with current data vector
            double[] netResult = net.simulate(data);
            System.out.println("    netOut " + Arrays.toString(netResult));

            for (int j = 0; j < data.length; j++) {
                net.neurons[0][j].updateWeight(data, outputs.get(pointer), netResult);
            }

        }

    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public String toString() {
        return "RBF";
    }

}
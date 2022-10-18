package rules;

import network.CPNetwork;
import network.Network;
import network.Neuron;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class CPN extends Rule{

    private String regex = "^([1-9][0-9]*+(\\s)?){3}$";
    private String prompt = "example: 2 2 2";

    private CPNetwork net;

    private double learnSpeed = 0.5;
    private double learnSlowDown = 0.002;
    private int  cycles = 1;

    @Override
    public void createNetwork(int[] inputNeuronsCount) {
        net = new CPNetwork(inputNeuronsCount);
        net.setRule(this);
    }

    @Override
    public void setNetwork(Network network) {
        net = (CPNetwork) network;
        net.setRule(this);
    }

    @Override
    public Network getNetwork() {
        return net;
    }

    @Override
    public void train(String dataset) {

        net.reset();

        ArrayList<double[]> parsedData = new ArrayList<>();
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

            parsedData.add(data);

        }

        double lSpeed = learnSpeed;

        while (lSpeed > 0) {

            Collections.shuffle(parsedData);
            for(double[] data: parsedData) {

                System.out.println(" input: " + Arrays.toString(data));

                //set input values
                for (int i = 0; i < net.neurons[0].length; i++) {
                    net.neurons[0][i].setNeuronValue(data[i]);
                }

                for (int i = 0; i < cycles; i++) {

                    //Kohonen layer
                    //Calculate length between neuron and input vector
                    double minSum = 0;
                    Neuron winner = net.neurons[1][0];
                    for (int j = 0; j < net.neurons[1].length; j++) {

                        double sum = 0;
                        for (int k = 0; k < net.neurons[0].length; k++) {
                            sum += Math.pow(data[k] - net.neurons[0][k].getWeight(j), 2);
                        }
                        sum = Math.sqrt(sum);
                        if (j == 0 || sum < minSum) {
                            minSum = sum;
                            winner = net.neurons[1][j];
                        }

                    }

                    for (int j = 0; j < net.neurons[0].length; j++) {
                        net.neurons[0][j].updateWeight(
                                winner.indexInLayer, lSpeed * (data[j] - net.neurons[0][j].getWeight(winner.indexInLayer))
                        );
                    }

                    //Grossberg layer
                    for(int j = 0; j < net.neurons[1].length; j++){
                        for(int k = 0; k < net.neurons[2].length; k++){
                            net.neurons[1][j].updateWeight(
                                    k, lSpeed * net.neurons[1][j].getNeuronValue() * (data[k] - net.neurons[1][j].getWeight(k))
                            );
                        }
                    }

                }

            }

            lSpeed -= learnSlowDown;

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
        return "CPN";
    }

}

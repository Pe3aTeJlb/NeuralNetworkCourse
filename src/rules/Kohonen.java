package rules;

import network.KohonenNetwork;
import network.Network;
import network.Neuron;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Kohonen extends Rule{

    private KohonenNetwork net;

    private double learnSpeed = 0.5;
    private double learnSlowDown = 0.001;
    private int  cycles = 1;

    public Kohonen(int[] inputNeuronsCount){
        net = new KohonenNetwork(inputNeuronsCount);
        net.setRule(this);

        net.neurons[0][0].updateWeight(0, 0.2);
        net.neurons[0][1].updateWeight(0, 0.6);
        net.neurons[0][2].updateWeight(0, 0.5);
        net.neurons[0][3].updateWeight(0, 0.9);

        net.neurons[0][0].updateWeight(1, 0.8);
        net.neurons[0][1].updateWeight(1, 0.4);
        net.neurons[0][2].updateWeight(1, 0.7);
        net.neurons[0][3].updateWeight(1, 0.3);

    }

    @Override
    public void setNetwork(Network network) {
        net = (KohonenNetwork) network;
        net.setRule(this);
    }

    @Override
    public Network getNetwork() {
        return net;
    }

    @Override
    public void train(String dataset) {

        //net.reset();

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

            for(double[] data: parsedData) {

                System.out.println(" input: " + Arrays.toString(data));

                //set input values
                for (int i = 0; i < net.neurons[0].length; i++) {
                    net.neurons[0][i].setNeuronValue(data[i]);
                }

                for (int i = 0; i < cycles; i++) {

                    //Calculate length between neuron and input vector
                    double minSum = 0;
                    Neuron winner = net.neurons[1][0];
                    for (int j = 0; j < net.neurons[1].length; j++) {

                        double sum = 0;
                        for (int k = 0; k < net.neurons[0].length; k++) {
                            sum += Math.pow(data[k] - net.neurons[0][k].getWeight(j), 2);
                        }
                        sum = Math.sqrt(sum);
                        System.out.println("neuron " + j + " len " + sum);
                        if (j == 0 || sum < minSum) {
                            minSum = sum;
                            winner = net.neurons[1][j];
                        }

                    }

                    for (int j = 0; j < net.neurons[0].length; j++) {
                        System.out.println("updw " + j + ": " + data[j]+ " " + net.neurons[0][j].getWeight(winner.indexInLayer));
                        net.neurons[0][j].updateWeight(
                                winner.indexInLayer, learnSpeed * (data[j] - net.neurons[0][j].getWeight(winner.indexInLayer))
                        );
                    }

                }

            }

            lSpeed -= learnSlowDown;

        }

        //Print weights vector
        double[] weights = new double[net.neurons[0].length];
        for(int i = 0; i < net.neurons[1].length; i++) {
            for (int j = 0; j < net.neurons[0].length; j++) {
                weights[j] = net.neurons[0][j].getWeight(i);
            }
            System.out.println(i + " weight vector: " + Arrays.toString(weights));
        }

    }

    @Override
    public String toString() {
        return "Kohonen Net";
    }

}

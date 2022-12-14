package rules;

import network.KohonenNetwork;
import network.Network;
import network.Neuron;

import java.io.InputStream;
import java.util.*;

public class Kohonen extends Rule{

    private String regex = "^([1-9][0-9]*+(\\s)?){2}$";
    private String prompt = "example: 4 2";

    private KohonenNetwork net;

    private double learnSpeed = 0.5;
    private double learnSlowDown = 0.002;
    private int  cycles = 1;

    @Override
    public void createNetwork(int[] inputNeuronsCount) {
        net = new KohonenNetwork(inputNeuronsCount);
        net.setRule(this);
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
                                winner.indexInLayer, lSpeed * (data[j] - net.neurons[0][j].getWeight(winner.indexInLayer))
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
    public String getRegex() {
        return regex;
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public String toString() {
        return "Kohonen Net";
    }

}

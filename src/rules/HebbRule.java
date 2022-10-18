package rules;

import network.Network;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class HebbRule extends Rule {

    private String regex = "^([1-9][0-9]*+(\\s)?)+$";
    private String prompt = "example: 2 ... 1";

    private Network net;


    @Override
    public void createNetwork(int[] inputNeuronsCount) {
        net = new Network(inputNeuronsCount);
        net.setRule(this);
    }

    @Override
    public void setNetwork(Network network) {
        net = network;
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



        for (int cnt = 0; cnt < 100; cnt++) {

            double[] data = parsedData.get(ThreadLocalRandom.current().nextInt(0, parsedData.size()));
            System.out.println(cnt + " input: " + Arrays.toString(data));

            //make desire output vector
            double[] desireOutput = Arrays.copyOfRange(data, data.length - net.outputSize, data.length);
            System.out.println("desireOutput " + Arrays.toString(desireOutput));

            //Simulate with current data vector
            double[] netResult = net.simulate(Arrays.copyOfRange(data, 0, data.length - net.outputSize));
            System.out.println("netOut " + Arrays.toString(netResult));

            //If net output != desire output then update weights
            if(Arrays.compare(netResult, desireOutput) != 0){

                for(int i = 0; i < net.neurons.length - 1; i++){
                    for(int j = 0; j < net.neurons[i].length; j++){
                        for(int k = 0; k < net.neurons[i][j].wCount; k++) {
                            for (int m = 0; m < desireOutput.length; m++) {
                                net.neurons[i][j].updateWeight(k, data[j] * desireOutput[m]);
                            }
                        }
                    }
                }

                net.updateBias(-data[data.length - 1]);

            }

            System.out.println("");

        }

        System.out.println("Bias " + net.getBias());

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
    public String toString(){
        return "Hebb rule";
    }

}
package rules;

import network.HopfieldNetwork;
import network.Network;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Hopfield extends Rule{

    private String regex = "^([1-9][0-9]*+(\\s)?){1}$";
    private String prompt = "example: 90";

    private HopfieldNetwork net;

    @Override
    public void createNetwork(int[] inputNeuronsCount) {
        net = new HopfieldNetwork(inputNeuronsCount);
        net.setRule(this);
    }

    @Override
    public void setNetwork(Network network) {
        net = (HopfieldNetwork) network;
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
            double[] data = new double[net.outputSize];
            for(int i = 0; i < buff.length; i++){
                data[i] = Double.parseDouble(buff[i]);
            }

            if(buff.length < net.outputSize){
                int diff = net.outputSize - buff.length;
                for(int i = buff.length - 1; i < diff; i++){
                    data[i] = 0;
                }
            }

            parsedData.add(data);

        }

        //foreach neuron
        for(int i = 0; i < net.neurons[0].length; i++){
            //foreach neuron weight
            for (int j = 0; j < net.neurons[0].length; j++){

                double newWeight = 0;
                for(int k = 0; k < parsedData.size(); k++){
                    newWeight += parsedData.get(k)[i] * parsedData.get(k)[j];
                }
                newWeight = newWeight / parsedData.get(0).length;
                net.neurons[0][i].updateWeight(j, newWeight);

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
        return "Hopfield";
    }

}

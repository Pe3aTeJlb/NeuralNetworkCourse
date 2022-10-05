package networks.hebbnet;

import networks.Network;
import java.io.*;
import java.util.Scanner;

public class HebbNet extends Network {

    private double bias;

    public HebbNet(int[] inputNeuronsCount) {
        super(inputNeuronsCount);
    }

    public void train(String dataset) {

        reset();

        InputStream inputStream = HebbNet.class.getResourceAsStream(dataset);
        Scanner reader = new Scanner(inputStream);
        while (reader.hasNextLine()) {

            String line = reader.nextLine();
            if(line.equals("")) continue;

            String[] data =  line.trim().split(" ");
            System.out.println("input: " + line);

            for(int i = 0; i < neurons[0].length; i++){
                neurons[0][i].updateWeight(Integer.parseInt(data[i]), Integer.parseInt(data[2]));
            }

            updateBias(Integer.parseInt(data[2]));

            /*
            int sim = simulate(data);
            if(sim != Integer.parseInt(data[2])){

                if(sim == 0){
                    System.out.println("increase");
                    for(int i = 0; i < neurons[0].length; i++){
                        neurons[0][i].increaseWeight(Integer.parseInt(data[i]));
                    }
                } else {
                    System.out.println("decrease");
                    for(int i = 0; i < neurons[0].length; i++){
                        neurons[0][i].decreaseWeight(Integer.parseInt(data[i]));
                    }
                }

            }

            System.out.println("bias " + getBias());
            updateBias(sim);


             */
        }

    }

    public void simulate(String input) {
        int sum = 0;
        String[] inputValues = input.trim().split(" ");
        for (int i = 0; i < neurons[0].length; i++) {
            sum += neurons[0][i].fire(Integer.parseInt(inputValues[i]));
        }
        System.out.println("sim sum " +sum + " " + bias);
        neurons[neurons.length-1][0].setNeuronValue(sum + bias >= 0 ? 1 : -1);
    }

    private int simulate(String[] input) {
        int sum = 0;
        for (int i = 0; i < neurons[0].length; i++) {
            sum += neurons[0][i].fire(Integer.parseInt(input[i]));
        }
        System.out.println("simulate " +(sum + bias >= 0 ? 1 : -1));
        return (sum + bias >= 0 ? 1 : -1);
    }

    private void updateBias(int targetValue) {
        setBias(getBias() + targetValue);
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    private void reset(){
        bias = 0;
        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){
                neurons[i][j].reset();
            }
        }
    }

    @Override
    public String toString(){
        return "Hebb rule";
    }

}
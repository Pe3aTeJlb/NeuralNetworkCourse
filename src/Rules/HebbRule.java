package Rules;

import network.Network;

import java.io.*;
import java.util.Scanner;

public class HebbRule extends Rule {

    @Override
    public void setNetwork(Network network) {
        this.net = network;
    }

    public void train(String dataset) {

        net.reset();

        InputStream inputStream = HebbRule.class.getResourceAsStream(dataset);
        Scanner reader = new Scanner(inputStream);
        while (reader.hasNextLine()) {

            String line = reader.nextLine();
            if(line.equals("")) continue;

            String[] data =  line.trim().split(" ");
            System.out.println("input: " + line);

            for(int i = 0; i < net.neurons[0].length; i++){
                net.neurons[0][i].updateWeight(
                        net.neurons[0][i].getWeight() + Integer.parseInt(data[i])*Integer.parseInt(data[2])
                );
            }

            net.updateBias(Integer.parseInt(data[2]));

        }

    }

    @Override
    public String toString(){
        return "Hebb rule";
    }

}
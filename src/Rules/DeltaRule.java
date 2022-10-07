package Rules;

import network.Network;

import java.io.InputStream;
import java.util.Scanner;

public class DeltaRule extends Rule {

    private double delta;
    private double learnSpeed = 5;

    @Override
    public void setNetwork(Network network) {
        this.net = network;
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
                net.neurons[0][i].updateWeight(
                        net.neurons[0][i].getWeight() + delta * Integer.parseInt(data[i]) * learnSpeed);
            }

            net.updateBias(Integer.parseInt(data[2]));

        }

    }

    @Override
    public String toString(){
        return "Delta rule";
    }

}

package Rules;

import network.Network;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Backpropagation extends Rule{

    private double delta;
    private double learnSpeed = 5;
    private int error;

    @Override
    public void setNetwork(Network network) {
        this.net = network;
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
                    net.neurons[0][i].updateWeight(
                            net.neurons[0][i].getWeight() + delta * Integer.parseInt(data[i]) * learnSpeed);
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

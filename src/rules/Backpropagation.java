package rules;

import network.BackprogNetwork;
import network.Network;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Backpropagation extends Rule{

    private BackprogNetwork net;

    private double learnSpeed = 0.5;
    private double netMeanSquaredError;

    public Backpropagation(int[] inputNeuronsCount){
        net = new BackprogNetwork(inputNeuronsCount);
        net.setRule(this);
    }

    @Override
    public void setNetwork(Network network) {
        net = (BackprogNetwork) network;
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

        netMeanSquaredError = 0;
        //int cnt = 0;
        for (int r = 0; r < 10000; r++) {
        //for(double[] data: parsedData) {
         //   do {
            double[] data = parsedData.get(ThreadLocalRandom.current().nextInt(0, parsedData.size()));
            System.out.println(r + " input: " + Arrays.toString(data));

           // for (int cnt = 0; cnt < 2; cnt++) {
                netMeanSquaredError = 0;
                //double[] data = parsedData.get(cnt);
                System.out.println(" input: " + Arrays.toString(data));

                //make desire output vector
                double[] desireOutput = Arrays.copyOfRange(data, data.length - net.outputSize, data.length);
                System.out.println("desireOutput " + Arrays.toString(desireOutput));

                //Simulate with current data vector
                double[] netResult = net.simulate(Arrays.copyOfRange(data, 0, data.length - net.outputSize));
                System.out.println("    netOut " + Arrays.toString(netResult));

                //Calculate mse error
                for (int i = 0; i < netResult.length; i++) {
                    netMeanSquaredError += 0.5 * Math.pow(desireOutput[i] - netResult[i], 2);
                }
                System.out.println("MSE " + netMeanSquaredError);


                //Error for each output neuron
                double[] outError = new double[net.outputSize];
                for (int i = 0; i < net.outputSize; i++) {
                    outError[i] = (desireOutput[i] - netResult[i]) * sigmoidDerivative(netResult[i]);
                }
                System.out.println("outError " + Arrays.toString(outError));

                //Error for hidden neuron
                double[][] hidError = new double[net.neurons.length - 2][];
                for(int i = net.neurons.length - 2; i > 0; i--){
                    hidError[i - 1] = new double[net.neurons[i].length];
                    for(int j = 0; j < net.neurons[i].length; j++){
                        for(int k = 0; k < net.neurons[i + 1].length; k++) {
                            if (i == net.neurons.length - 2)
                                hidError[i-1][j] += net.neurons[i][j].getWeight(k) * outError[k];
                            else hidError[i-1][j] += net.neurons[i][j].getWeight(k) * hidError[i][k];
                        }
                        hidError[i-1][j] *= sigmoidDerivative(net.neurons[i][j].getNeuronValue());
                    }
                }
                //System.out.println("hidError " + Arrays.toString(Arrays.toString(hidError)));

                //Update weight
                for(int i = net.neurons.length - 2; i >= 0; i--){
                    for(int j = 0; j < net.neurons[i].length; j++){
                        for(int k = 0; k < net.neurons[i + 1].length; k++) {
                            if (i == net.neurons.length - 2) net.neurons[i][j].updateWeight(k, learnSpeed*outError[k]*net.neurons[i][j].getNeuronValue());
                            else net.neurons[i][j].updateWeight(k, learnSpeed*hidError[i][k]*net.neurons[i][j].getNeuronValue());
                        }
                    }
                }

                //Update threshold weights
                for(int i = net.neurons.length - 1; i > 0; i--){
                    for(int j = 0; j < net.neurons[i].length; j++){
                        if (i == net.neurons.length - 1) net.neurons[i][j].threshold += learnSpeed * outError[j];
                        else net.neurons[i][j].threshold -= learnSpeed * hidError[i-1][j];
                    }
                }

                System.out.println("");
                //cnt++;

            }
           // } while (cnt <= 3 || netMeanSquaredError > 0.05);
      //  }
    }

    private double sigmoidDerivative(double val) {
        return (val * (1.0 - val));
    }

    @Override
    public String toString() {
        return "BackPropagation";
    }

}

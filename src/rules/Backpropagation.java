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

        int cnt = 0;
      //  do {

            double[] data = parsedData.get(ThreadLocalRandom.current().nextInt(0, parsedData.size()));
            System.out.println(cnt + " input: " + Arrays.toString(data));

            //make desire output vector
            double[] desireOutput = Arrays.copyOfRange(data, data.length - net.outputSize, data.length);
            System.out.println("desireOutput " + Arrays.toString(desireOutput));

            //Simulate with current data vector
            double[] netResult = net.simulate(Arrays.copyOfRange(data, 0, data.length - net.outputSize));
            System.out.println("netOut " + Arrays.toString(netResult));

            //Calculate mse error
            netMeanSquaredError = 0;
            for (int i = 0; i < netResult.length; i++) {
                netMeanSquaredError += 0.5 * Math.pow(desireOutput[i] - netResult[i], 2);
            }
            System.out.println("MSE " + netMeanSquaredError);


            //Error for each output neuron
            double[] outError = new double[net.neurons[net.neurons.length - 1].length];
            for(int i = 0; i < net.neurons[net.neurons.length - 1].length; i++){
                outError[i] = (desireOutput[i] - netResult[i]) * sigmoidDerivative(netResult[i]);
            }
            System.out.println("outError " + Arrays.toString(outError));

            //Error for hidden neuron
            double[] hidError = new double[net.neurons[net.neurons.length - 2].length];
            for(int i = 0; i < net.neurons[net.neurons.length - 2].length; i++){
                hidError[i] = 0;
                for(int j = 0; j < net.outputSize; j++){
                    hidError[i] += net.neurons[net.neurons.length - 2][i].getWeight(j) * outError[j];
                }
                hidError[i] *= sigmoidDerivative(net.neurons[net.neurons.length - 2][i].getNeuronValue());
            }
            System.out.println("hidError " + Arrays.toString(hidError));

            //Update weights for layers
            for(int i = 0; i < net.outputSize; i++){
                for(int j = 0; j < net.neurons[net.neurons.length-2].length; j++) {
                    net.neurons[net.neurons.length-2][j].updateWeight(
                            i, learnSpeed * outError[i] * net.neurons[net.neurons.length-2][j].getNeuronValue()
                    );
                }
            }

            for(int i = 0; i < net.neurons[net.neurons.length-2].length; i++){
                for(int j = 0; j < net.neurons[0].length; j++){
                    net.neurons[0][j].updateWeight(
                            i,  learnSpeed * hidError[i] * net.neurons[i][j].getNeuronValue()
                    );
                }
            }

            System.out.println("");
            cnt++;

       // } while (netMeanSquaredError > 0.01);

    }

    private double sigmoidDerivative(double val) {
        return (val * (1.0 - val));
    }

    @Override
    public String toString() {
        return "BackPropagation";
    }

}

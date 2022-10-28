package rules;

import network.CNNetwork;
import network.Network;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class CNN extends Rule{

    private String regex = "^([1-9][0-9]*+(\\s)?)+$";
    private String prompt = "example: 1250 128 64 10";

    private double[][] verticalFilter = new double[][]{{1, 0, -1}, {1, 0, -1}, {1, 0, -1}};
    private double[][] horizontalFilter = new double[][]{{1, 1, 1}, {0, 0, 0}, {-1, -1, -1}};

    private ArrayList<double[][]> filters = new ArrayList<>(Arrays.asList(verticalFilter, horizontalFilter));
    private ArrayList<double[][]> convolutions = new ArrayList<>();
    private ArrayList<double[][]> maxPools = new ArrayList<>();
    private int poolSize = 2;
    private ArrayList<Double> flatter = new ArrayList<>();

    private double learnSpeed = 0.5;
    private double netMeanSquaredError;

    private CNNetwork net;

    @Override
    public void createNetwork(int[] inputNeuronsCount) {
        net = new CNNetwork(inputNeuronsCount);
        net.setRule(this);
    }

    @Override
    public void setNetwork(Network network) {
        net = (CNNetwork) network;
        net.setRule(this);
    }

    @Override
    public Network getNetwork() {
        return net;
    }

    @Override
    public void train(String dataset) {

        net.reset();
        
        InputStream inputStream = HebbRule.class.getResourceAsStream(dataset);
        Scanner reader = new Scanner(inputStream);
        while (reader.hasNextLine()) {

            String line = reader.nextLine();
            if(line.equals("")) continue;

            //input input to double vector
            String[] buff = line.trim().split(",");
            double[][] data = new double[28][28];
            int output =Integer.parseInt(buff[0]);
            buff = Arrays.copyOfRange(buff, 1, buff.length);

            for(int i = 0; i < buff.length / 28; i++){
                for(int j = 0; j < 28; j++){
                    data[i][j] = Double.parseDouble(buff[i*28+j]);
                }
            }

            /*
            for(int i = 0; i < data.length; i++){
                System.out.println(Arrays.toString(data[i]));
            }
            System.out.println("\n");
            */

            for(double[][] filter: filters){

                double[][] convolution = new double[data.length - 2][data.length - 2];

                for(int i = 0; i < convolution.length; i++){
                    for(int j = 0; j < convolution[i].length; j++){

                        double cell = 0;
                        //for filter height
                        for(int k = i; k < i + filter.length; k++){
                            for(int m = j; m < j + filter.length; m++){
                               // System.out.print(data[k][m] + "(" +k+" "+m+")"+" * " + filter[k - i][m - j] + " | ");
                                cell += data[k][m] * filter[k - i][m - j];
                            }
                           // System.out.println(" ");
                        }
                        convolution[i][j] = cell;
                       // System.out.println("cell "+ i+ " " +j  + " is " + cell+ "\n");

                    }
                    //System.out.println("\n");
                }


                //ReLu
                for(int i = 0; i < convolution.length; i++){
                    for(int j = 0; j < convolution[i].length; j++){
                        convolution[i][j] = Math.max(0, convolution[i][j]);
                        //System.out.print(convolution[i][j] + " ");
                    }
                    //System.out.println("");
                }
                //System.out.println("\n\n");

                convolutions.add(convolution);

                //Max pool
                double[][] pool = new double[convolution.length - 1][convolution.length - 1];

                for(int i = 0; i < pool.length; i++){
                    for(int j = 0; j < pool[i].length; j++){

                        double maxCell = 0;
                        //for filter height
                        for(int k = i; k < i + poolSize; k++){
                            for(int m = j; m < j + poolSize; m++){
                                //System.out.print(convolution[k][j] + "(" +k+" "+m+")" + " | ");
                                if(maxCell < convolution[k][m]) maxCell = convolution[k][m];
                            }
                             //System.out.println(" ");
                        }
                        pool[i][j] = maxCell;
                        flatter.add(maxCell);
                        //System.out.println("maxCell "+ i+ " " +j  + " is " + maxCell+ "\n");

                    }
                    //System.out.println("\n");
                }


                for(int i = 0; i < pool.length; i++){
                    for(int j = 0; j < pool[i].length; j++){
                        pool[i][j] = Math.max(0, pool[i][j]);
                       // System.out.print(pool[i][j] + " ");
                    }
                   // System.out.println("");
                }

                maxPools.add(pool);

            }

            //System.out.println(flatter.size());
            //System.out.println(flatter);

            //Backprop

            netMeanSquaredError = 0;
            //System.out.println(" input: " + Arrays.toString(data));

            //make desire output vector
            double[] desireOutput = new double[10];
            Arrays.fill(desireOutput, 0);
            desireOutput[output] = 1;
            System.out.println("desireOutput " + Arrays.toString(desireOutput));

            //Simulate with current data vector
            double[] netResult = net.simTest(flatter);
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


        }

    }

    private double sigmoidDerivative(double val) {
        return (val * (1.0 - val));
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
        return "CNN";
    }

}

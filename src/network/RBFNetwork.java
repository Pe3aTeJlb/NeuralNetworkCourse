package network;

public class RBFNetwork extends Network{

    private double bias = 0.49;
    public RBFNeuron[][] neurons;

    public RBFNetwork(int[] inputNeuronsCount){

        super(inputNeuronsCount);

        neurons = new RBFNeuron[inputNeuronsCount.length][];

        for(int i = 0; i < inputNeuronsCount.length; i++){
            neurons[i] = new RBFNeuron[inputNeuronsCount[i]];
        }

        for (int i = 0; i < inputNeuronsCount.length; i++) {
            for(int j = 0; j < inputNeuronsCount[i]; j++) {
                neurons[i][j] = new RBFNeuron();
            }
        }

        ((Network) this).neurons = neurons;

    }

    @Override
    public void train(String dataset) {
        rule.train(dataset);
    }

    @Override
    public double simulate(String[] input) {

        double[] inputs = new double[input.length];
        for(int i = 0; i < input.length; i++){
            inputs[i] = Double.parseDouble(input[i]);
        }

        double netOutput = 0;
        for(int i = 0; i < inputs.length; i++){
            netOutput += neurons[0][i].fire(inputs);
        }

        neurons[neurons.length-1][0].setNeuronValue(netOutput > bias ? 1 : -1);
        //neurons[neurons.length-1][0].setNeuronValue(netOutput);

        return netOutput;

    }

    @Override
    public void reset() {
        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){
                neurons[i][j].reset();
            }
        }
    }

}

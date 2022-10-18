package network;

import java.util.function.Function;

public class RNeuron extends Neuron{

    private Function<Double, Double> activationFunc = (x) -> Math.signum(x);

    public RNeuron(int indexInLayer, int weightCount) {
        super(indexInLayer, weightCount);
    }

    @Override
    public double fire(int index){
        //System.out.println(neuronValue + " " + weight[index] + " " + neuronValue * weight[index]);
        return neuronValue * weight[index];
    }

    @Override
    public void activate(double input){
        neuronValue = activationFunc.apply(input);
    }

}

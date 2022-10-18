package network;

import java.util.function.Function;

public class Outstar extends Neuron{

    private Function<Double, Double> activationFunc = (x) -> x;

    public Outstar(int indexInLayer, int weightCount) {
        super(indexInLayer, weightCount);
    }

    @Override
    public void activate(double input){
        neuronValue = activationFunc.apply(input);
    }

}

package network;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

public class Instar extends Neuron{

    private Function<Double, Double> activationFunc = (x) -> x;

    private final double rangeMin = -0.4;
    private final double rangeMax = 0.4;

    public Instar(int indexInLayer, int weightCount) {
        super(indexInLayer, weightCount);
    }

    @Override
    public double fire(int index){
        return neuronValue * weight[index];
    }

    @Override
    public void activate(double input){
        neuronValue = activationFunc.apply(input);
    }

    @Override
    public void reset(){
        neuronValue = 0;
        Arrays.fill(weight, 0.0);
        Random r = new Random();
        for(int i = 0; i < wCount; i++){
            weight[i] = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        }
    }

}

package network;

import javafx.scene.canvas.GraphicsContext;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiFunction;

public class CNNDefNeuron extends Neuron {

    //must be softmax
    private BiFunction<Double, Double[], Double> activationFunc = (x, v) -> {
        double sum = 0;
        for(int i = 0; i < v.length; i++){
            sum += Math.exp(v[i]);
        }
        return Math.exp(x)/sum;
    };

    private final double rangeMin = -0.4;
    private final double rangeMax = 0.4;

    public CNNDefNeuron(int indexInLayer, int weightCount) {
        super(indexInLayer, weightCount);
    }

    @Override
    public double fire(int index){
        return neuronValue * weight[index];
    }

    public void activate(double input, Double[] v){
        neuronValue = activationFunc.apply(input, v);
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

    @Override
    protected void draw(GraphicsContext gc, double posX, double posY){
        /*
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(posX, posY, radius, radius);
        gc.fillOval(posX, posY, radius, radius);
        gc.setFill(Color.BLACK);
        gc.fillText(String.format("%.4f", neuronValue), posX + radius/2, posY + radius/2);
        pos = new Point2D(posX, posY);

         */
    }

}

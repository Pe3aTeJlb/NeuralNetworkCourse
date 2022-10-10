package network;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

public class BackprogNeuron extends Neuron{

    private Function<Double, Double> activationFunc = (x) -> 1 / (1 + Math.pow(Math.E,-x));
    private final double rangeMin = -0.4;
    private final double rangeMax = 0.4;

    public double threshold;

    public BackprogNeuron(int layerIndex, int weightCount){
        super(layerIndex, weightCount);
    }

    @Override
    public double fire(int index){
        return neuronValue * weight[index];
    }

    @Override
    public void activate(double input){
        neuronValue = activationFunc.apply(input);
        System.out.println("actiovation " + neuronValue + " with input " + input);
    }
/*
    @Override
    public double fire(int index, double input){
        return activationFunc.apply(input) * weight[index];
    }
*/
    @Override
    public void reset(){
        neuronValue = 0;
        Arrays.fill(weight, 0.0);
        Random r = new Random();
        for(int i = 0; i < wCount; i++){
            weight[i] = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
            threshold = 1;
        }
    }

    @Override
    protected void draw(GraphicsContext gc, double posX, double posY){
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(posX, posY, radius, radius);
        gc.fillOval(posX, posY, radius, radius);
        gc.setFill(Color.BLACK);
        gc.fillText(String.format("%.4f", neuronValue), posX + radius/2, posY + radius/2);
        pos = new Point2D(posX, posY);
    }

}

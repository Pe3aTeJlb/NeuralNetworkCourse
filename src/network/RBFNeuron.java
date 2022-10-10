package network;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Random;

public class RBFNeuron extends Neuron{

    private double[] center = new double[2];
    private double sigma = 0.5;
    private double n1 = 0.1;
    private double n2 = 0.1;

    private final double rangeMin = -0.5;
    private final double rangeMax = 0.5;

    private double[] neuronVector = new double[2];

    public RBFNeuron(int layerIndex, int weightCount){
        super(layerIndex, weightCount);
    }

    public void updateWeight(double[] input, double[] desired, double[] netOutput){

        double phi = phi(input);
        //double diffOutput = desired - netOutput;
        double[] delta = new double[desired.length];
        for(int i = 0; i < delta.length; i++){
            delta[i] = desired[i] - netOutput[i];
        }

        for(int i = 0; i < desired.length; i++) {
            for (int j = 0; j < center.length; j++) {
                center[j] = center[j] + (n1 * delta[i] * weight[i] * phi * (input[i] - center[i]) / (sigma * sigma));
            }

            weight[i] += n2 * delta[i] * phi;
        }

    }

    public double fire(int index, double[] inputValue) {
        neuronVector = inputValue;
        return phi(inputValue) * weight[index];
    }


    private double phi(double[] input){
        double distance = 0;
        for(int i = 0; i < center.length; i++) {
            distance += Math.pow(input[i] - center[i], 2);
        }
        return Math.pow(Math.E,-distance / (2 * Math.pow(sigma, 2)));
    }

    public void setNeuronVector(double[] neuronVector){
        this.neuronVector = neuronVector;
    }

    @Override
    public void reset(){
        neuronValue = 0;
        Arrays.fill(neuronVector, 0);
        Arrays.fill(center, 0);
        Random r = new Random();
        for(int i = 0; i < wCount; i++){
            weight[i] = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        }
    }

    public void setCenter(double[] center){
        this.center = center;
    }

    @Override
    public void draw(GraphicsContext gc, double posX, double posY){
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(posX, posY, radius, radius);
        gc.fillOval(posX, posY, radius, radius);
        gc.setFill(Color.BLACK);
        if(neuronValue != 0) {
            gc.fillText(String.format("%.4f", neuronValue), posX + radius/2, posY + radius/2);
        } else {
            gc.fillText(Arrays.toString(neuronVector), posX + radius / 2, posY + radius / 2);
        }
        pos = new Point2D(posX, posY);
    }

}

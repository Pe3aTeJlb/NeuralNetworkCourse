package network;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class RBFNeuron extends Neuron{

    private double[] center = new double[2];
    private double sigma = 0.5;
    private double n1 = 0.5;
    private double n2 = 0.5;

    private double[] neuronVector;

    public void updateWeight(double[] input, double desired, double netOutput){

        double phi = phi(input);
        double diffOutput = desired - netOutput;

        for(int i = 0; i < center.length; i++) {
            center[i] = center[i] + (n1 * diffOutput * weight * phi * (input[i] - center[i]) / (sigma * sigma));
        }

        weight += n2 * diffOutput * phi;

    }

    public double fire(double[] inputValue) {
        neuronVector = inputValue;
        return phi(inputValue) * weight;
    }


    private double phi(double[] input){
        double distance = 0;
        for(int i = 0; i < center.length; i++) {
            distance += Math.pow(input[i] - center[i], 2);
        }
        return Math.pow(Math.E,-distance / (2 * Math.pow(sigma, 2)));
    }

    @Override
    public void reset(){
        neuronValue = 0;
        neuronVector = new double[2];
        weight = 0;
        center = new double[center.length];
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
            gc.fillText(Double.toString(neuronValue), posX + radius/2, posY + radius/2);
        } else {
            gc.fillText(Arrays.toString(neuronVector), posX + radius / 2, posY + radius / 2);
        }
        pos = new Point2D(posX, posY);
    }

}
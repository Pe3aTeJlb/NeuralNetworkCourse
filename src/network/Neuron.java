package network;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.function.Function;

public class Neuron {

    public static final double radius = 100;

    protected Point2D pos;
    public int indexInLayer;
    public int wCount;
    protected double[] weight;
    protected double neuronValue;

    private Function<Double, Double> activationFunc = (x) -> x > 0 ? 1.0 : -1.0;

    public Neuron(int indexInLayer, int weightCount){
        this.indexInLayer = indexInLayer;
        wCount = weightCount;
        weight = new double[weightCount];
    }

    public void updateWeight(int index, double addWeight){
        this.weight[index] += addWeight;
        //System.out.println("new weight " + weight[index]);
    }

    public void normalyzeWeight(int index, double totalSum){
        weight[index] = weight[index] / totalSum;
    }

    public double getWeight(int index) {
        return this.weight[index];
    }

    public double fire(int index){
        System.out.println(neuronValue + " " + weight[index]);
        return neuronValue * weight[index];
    }

    public double fire(int index, double input){
        return activationFunc.apply(input) * weight[index];
    }

    public void activate(double input){
        neuronValue = activationFunc.apply(input);
    }

    public void reset(){
        neuronValue = 0;
        Arrays.fill(weight, 0.0);
    }


    public void setNeuronValue(double value){neuronValue = value;}
    public double getNeuronValue(){return neuronValue;}


    protected void draw(GraphicsContext gc, double posX, double posY){
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(posX, posY, radius, radius);
        gc.fillOval(posX, posY, radius, radius);
        gc.setFill(Color.BLACK);
        gc.fillText(Double.toString(neuronValue), posX + radius/2, posY + radius/2);
        pos = new Point2D(posX, posY);
    }

    public Point2D getPos(){
        return pos;
    }

}

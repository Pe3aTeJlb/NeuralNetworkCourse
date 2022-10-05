package networks;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Neuron {

    public static final double radius = 100;
    private Point2D pos;
    private double weight;
    private int neuronValue;

    public void updateWeight(int input, int target){
        weight += input * target;
    }

    /*
    public void decreaseWeight(int input) {
        weight -= input;
        System.out.println(weight);
    }

    public void increaseWeight(int input) {
        weight += input;
        System.out.println(weight);
    }*/

    public double getWeight() {
        return this.weight;
    }

    public double fire(int inputValue) {
        neuronValue = inputValue;
        return inputValue * weight;
    }

    public void setNeuronValue(int neuronValue){
        this.neuronValue = neuronValue;
    }

    public void reset(){
        neuronValue = 0;
        weight = 0;
    }

    public void draw(GraphicsContext gc, double posX, double posY){
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(posX, posY, radius, radius);
        gc.fillOval(posX, posY, radius, radius);
        gc.setFill(Color.BLACK);
        gc.fillText(Integer.toString(neuronValue), posX + radius/2, posY + radius/2);
        pos = new Point2D(posX, posY);
    }

    public Point2D getPos(){
        return pos;
    }

}
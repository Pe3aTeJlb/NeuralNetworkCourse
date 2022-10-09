package network;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Neuron {

    public static final double radius = 100;

    public Point2D pos;
    public double weight;
    public double neuronValue;

    public void updateWeight(double weight){
        this.weight = weight;
    }

    public double getWeight() {
        return this.weight;
    }

    public double fire(){
        return neuronValue;
    }

    public abstract void reset();


    public void setNeuronValue(double neuronValue){
        this.neuronValue = neuronValue;
    }

    public double getNeuronValue(){return neuronValue;}


    public void draw(GraphicsContext gc, double posX, double posY){
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

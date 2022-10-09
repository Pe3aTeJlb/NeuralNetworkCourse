package network;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SimpleNeuron extends Neuron{

    @Override
    public void updateWeight(double addWeight){
        this.weight += addWeight;
        System.out.println("new weight " + weight);
    }

    public double getWeight() {
        return this.weight;
    }

    public double fire(double inputValue) {
        neuronValue = inputValue;
        return inputValue * weight;
    }

    @Override
    public void reset(){
        neuronValue = 0;
        weight = 0;
    }


    public void setNeuronValue(int neuronValue){
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

}
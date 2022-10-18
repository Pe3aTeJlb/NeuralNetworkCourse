package network;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Random;

public class KohonenNeuron extends Neuron{

    private final double rangeMin = -0.4;
    private final double rangeMax = 0.4;
    protected double[] neuronVector = new double[1];

    public KohonenNeuron(int indexInLayer, int weightCount) {
        super(indexInLayer, weightCount);
    }

    public void setNeuronVector(double[] value){neuronVector = value;}
    public double[] getNeuronVector(){return neuronVector;}

    @Override
    public void reset(){
        neuronValue = 0;
        Arrays.fill(neuronVector, 0.0);
        Arrays.fill(weight, 0.0);
        Random r = new Random();
        for(int i = 0; i < wCount; i++){
            weight[i] = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        }
    }

    @Override
    public void draw(GraphicsContext gc, double posX, double posY){
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(posX, posY, radius, radius);
        gc.fillOval(posX, posY, radius, radius);
        gc.setFill(Color.BLACK);
        gc.fillText(Arrays.toString(neuronVector), posX + radius / 2, posY + 20 + radius / 2);
        gc.fillText(String.format("%.4f", neuronValue), posX + radius/2, posY - 20 + radius/2);
        pos = new Point2D(posX, posY);
    }

}

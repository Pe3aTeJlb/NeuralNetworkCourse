package network;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.Arrays;

public class HopfieldNetwork extends Network{

    public RNeuron[][] neurons;

    public HopfieldNetwork(int[] layersDesk) {

        super(layersDesk);

        this.neurons = new RNeuron[layersDesk.length][];
        for(int i = 0; i < layersDesk.length; i++){
            neurons[i] = new RNeuron[layersDesk[i]];
        }

        for (int i = 0; i < layersDesk.length; i++) {
            for(int j = 0; j < layersDesk[i]; j++) {
                neurons[i][j] = new RNeuron(j, layersDesk[i]);
            }
        }

        ((Network) this).neurons = neurons;

    }

    @Override
    public double[] simulate(String[] input){
        double[] data = new double[input.length];
        for(int i = 0; i < input.length; i++){
            data[i] = Double.parseDouble(input[i]);
        }
        return simulate(data);
    }

    @Override
    public double[] simulate(double[] input){

        clearNeurons();

        //set init neurons values
        for(int i = 0; i < neurons[0].length; i++) {
            if (i < input.length) neurons[0][i].setNeuronValue(input[i]);
            else                  neurons[0][i].setNeuronValue(0);
        }

        drawNetVals();

        double[] prevResult;

        do {

            prevResult = getOutput();

            //calculate new state
            for (int i = 0; i < neurons[0].length; i++) {
                double layerSum = 0;
                for (int j = 0; j < neurons[0].length; j++) {
                    layerSum += neurons[0][j].fire(i);
                }
                neurons[0][i].activate(layerSum);
            }

            drawNetVals();

            System.out.println(Arrays.toString(prevResult));
            System.out.println(Arrays.toString(getOutput()));

        } while (!Arrays.equals(getOutput(), prevResult));


        return getOutput();

    }

    private void drawNetVals(){

        int j = 0;
        String outLine = "";
        for(int i = 0; i < outputSize; i++){
            if (j == 7) j = 0;
            if((int) neurons[neurons.length - 1][i].getNeuronValue() == 1){
                outLine += (int) neurons[neurons.length - 1][i].getNeuronValue() + " ";
            } else {
                outLine += "  ";
            }
            if(j == 6){
                System.out.println(outLine);
                outLine = "";
            }
            j++;
        }

    }

    private double[] getOutput(){
        double[] output = new double[outputSize];
        for(int i = 0; i < outputSize; i++){
            output[i] = neurons[neurons.length - 1][i].getNeuronValue();
        }
        return output;
    }

    //Drawing
    public double layerStep = Neuron.radius + 200;
    public double neuronStep = Neuron.radius + 150;

    @Override
    public void draw(GraphicsContext gc){

        double centerY = gc.getCanvas().getHeight() / 2;

        double posX = 150;
        double posY = centerY - (inputSize * neuronStep) / 2;

        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){
                neurons[i][j].draw(gc, posX, posY);
                posY += neuronStep;
            }
            posY = centerY - (inputSize * neuronStep) / 2;
            posX += layerStep;
        }

        gc.setFill(Color.BLACK);
        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){
                for(int k = 0; k < neurons[i].length; k++) {
                    connectNeurons(gc, neurons[i][j], neurons[i][k]);
                }
            }
        }

        posX = 150;
        posY = centerY - (inputSize * neuronStep) / 2;

        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){
                neurons[i][j].draw(gc, posX, posY);
                posY += neuronStep;
            }
            posY = centerY - (inputSize * neuronStep) / 2;
            posX += layerStep;
        }

    }

    private static final Font FONT = Font.font("serif", FontWeight.BOLD, FontPosture.REGULAR, 25);
    @Override
    protected void connectNeurons(GraphicsContext gc, Neuron n1, Neuron n2){
       // if (n1.getWeight(n2.indexInLayer) == 0) return;
        double offset = Neuron.radius / 2;
        Point2D p1 = n1.getPos();
        Point2D p2 = n2.getPos();
        gc.setFont(FONT);

        gc.strokeLine(p1.getX() + offset, p1.getY() + offset, p1.getX() + offset + 100, p1.getY() + offset);
        gc.strokeLine(p1.getX() + offset + 100, p1.getY() + offset, p1.getX() + offset + 100, p1.getY() + offset - 50);
        gc.strokeLine(p1.getX() + offset + 100, p1.getY() + offset - 50, p1.getX() + offset - 250, p1.getY() + offset - 50);
        gc.strokeLine(p1.getX() + offset - 250, p1.getY() + offset - 50, p2.getX() + offset, p2.getY() + offset);

        gc.fillText("w "+ n1.getWeight(n2.indexInLayer),
                (p1.getX() + offset - 250) + (((p2.getX() + offset) - (p1.getX() + offset - 250))/2),
                (p1.getY() + offset + 50) + (((p2.getY() + offset) - (p1.getY() + offset + 50))/2)
        );

    }


}

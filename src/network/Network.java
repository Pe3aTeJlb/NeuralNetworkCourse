package network;

import rules.Rule;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Network {

    public double bias;
    public int inputSize;
    public int outputSize;
    public Neuron[][] neurons;

    public Rule rule;

    public Network(int[] layersDesk){

        this.inputSize = layersDesk[0];
        this.outputSize = layersDesk[layersDesk.length - 1];

        this.neurons = new Neuron[layersDesk.length][];
        for(int i = 0; i < layersDesk.length; i++){
            neurons[i] = new Neuron[layersDesk[i]];
        }

        for (int i = 0; i < layersDesk.length; i++) {
            for(int j = 0; j < layersDesk[i]; j++) {
                if (i < layersDesk.length - 1) neurons[i][j] = new Neuron(j, layersDesk[i + 1]);
                else                           neurons[i][j] = new Neuron(j,0);
            }
        }

    }

    public void setRule(Rule rule){
        this.rule = rule;
    }

    public Rule getRule(){
        return rule;
    }


    public void train(String dataset){
        rule.train(dataset);
    };

    public double[] simulate(String[] input){
        double[] data = new double[input.length];
        for(int i = 0; i < input.length; i++){
            data[i] = Double.parseDouble(input[i]);
        }
        return simulate(data);
    }

    public double[] simulate(double[] input){
        //Update input layers weight
        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){

                if (i == 0)  { //input layer
                    neurons[i][j].setNeuronValue(input[j]);
                } else { //hidden layer and output
                    double layerSum = 0;
                    for(int k = 0; k < neurons[i - 1].length; k++){
                        layerSum += neurons[i - 1][k].fire(j);
                    }
                    neurons[i][j].activate(layerSum - bias);
                }

            }
        }

        //Output layer to vector
        double[] result = new double[outputSize];
        for(int i = 0; i < outputSize; i++){
            result[i] = neurons[neurons.length - 1][i].getNeuronValue();
        }
        return result;
    }

    public void updateBias(double targetValue) {
        setBias(getBias() + targetValue);
        System.out.println("new bias " + getBias());
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public void reset(){
        bias = 0;
        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){
                neurons[i][j].reset();
            }
        }
    }


    //Drawing
    public double layerStep = Neuron.radius + 200;
    public double neuronStep = Neuron.radius + 100;

    public void draw(GraphicsContext gc){

        double centerY = gc.getCanvas().getWidth() / 2;

        double posX = 150;
        double posY = centerY + (inputSize * neuronStep) / 2;


        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){
                neurons[i][j].draw(gc, posX, posY);
                posY += neuronStep;
            }
            posY = centerY + (inputSize * neuronStep) / 2;
            posX += layerStep;
        }

        gc.setFill(Color.BLACK);
        for(int i = 0; i < neurons.length - 1; i++){
            for(int j = 0; j < neurons[i].length; j++){
                for (int k = 0; k < neurons[i+1].length; k++) {
                    connectNeurons(gc, neurons[i][j], neurons[i+1][k]);
                }
            }
        }

        posX = 150;
        posY = centerY + (inputSize * neuronStep) / 2;

        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){
                neurons[i][j].draw(gc, posX, posY);
                posY += neuronStep;
            }
            posY = centerY + (inputSize * neuronStep) / 2;
            posX += layerStep;
        }

    }

    private static final Font FONT = Font.font("serif", FontWeight.BOLD, FontPosture.REGULAR, 25);
    protected void connectNeurons(GraphicsContext gc, Neuron n1, Neuron n2){
        if (n1.getWeight(n2.indexInLayer) == 0) return;
        double offset = Neuron.radius / 2;
        Point2D p1 = n1.getPos();
        Point2D p2 = n2.getPos();
        gc.setFont(FONT);
        gc.fillText("w "+ n1.getWeight(n2.indexInLayer), (p1.getX()+p2.getX()+2*offset)/2, (p1.getY()+p2.getY()+2*offset)/2);
        gc.strokeLine(p1.getX() + offset, p1.getY() + offset, p2.getX() + offset, p2.getY() + offset);
    }

}

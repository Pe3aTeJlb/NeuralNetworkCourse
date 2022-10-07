package network;

import Rules.Rule;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Network {

    private int inputSize;
    public final Neuron[][] neurons;
    private double bias;
    private Rule rule;

    public Network(int[] inputNeuronsCount){
        this.inputSize = inputNeuronsCount[0];
        neurons = new Neuron[inputNeuronsCount.length][];
        for(int i = 0; i < inputNeuronsCount.length; i++){
            neurons[i] = new Neuron[inputNeuronsCount[i]];
        }
        for (int i = 0; i < inputNeuronsCount.length; i++) {
            for(int j = 0; j < inputNeuronsCount[i]; j++) {
                neurons[i][j] = new Neuron();
            }
        }
    }

    public void setRule(Rule rule){
        this.rule = rule;
        rule.setNetwork(this);
    }

    public Rule getRule(){
        return rule;
    }

    public void train(String dataset){
        rule.train(dataset);
    }

    public int simulate(String[] input){

        int sum = 0;
        //Set input neurons value
        for (int i = 0; i < neurons[0].length; i++) {
            sum += neurons[0][i].fire(Integer.parseInt(input[i]));
        }

        System.out.println("simulate " +(sum + bias >= 0 ? 1 : -1));
        neurons[neurons.length-1][0].setNeuronValue(sum + bias >= 0 ? 1 : -1);

        return (sum + bias >= 0 ? 1 : -1);

    }

    public void updateBias(int targetValue) {
        setBias(getBias() + targetValue);
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
    private void connectNeurons(GraphicsContext gc, Neuron n1, Neuron n2){
        double offset = Neuron.radius / 2;
        Point2D p1 = n1.getPos();
        Point2D p2 = n2.getPos();gc.setFont(FONT);
        gc.fillText("w "+ n1.getWeight(), (p1.getX()+p2.getX()+2*offset)/2, (p1.getY()+p2.getY()+2*offset)/2);
        gc.strokeLine(p1.getX() + offset, p1.getY() + offset, p2.getX() + offset, p2.getY() + offset);
    }

}
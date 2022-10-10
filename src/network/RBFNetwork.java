package network;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class RBFNetwork extends Network{

    private double bias = 0.49;
    public RBFNeuron[][] neurons;

    public RBFNetwork(int[] layersDesk){

        super(layersDesk);

        neurons = new RBFNeuron[layersDesk.length][];
        for(int i = 0; i < layersDesk.length; i++){
            neurons[i] = new RBFNeuron[layersDesk[i]];
        }

        for (int i = 0; i < layersDesk.length; i++) {
            for(int j = 0; j < layersDesk[i]; j++) {
                if (i < layersDesk.length - 1) neurons[i][j] = new RBFNeuron(j, layersDesk[i + 1]);
                else                           neurons[i][j] = new RBFNeuron(j,0);
            }
        }

        ((Network) this).neurons = neurons;

    }

    @Override
    public void train(String dataset) {
        rule.train(dataset);
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
    public double[] simulate(double[] inputs) {

        //Update input layers weight
        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){

                if (i == 0)  { //input layer
                    neurons[i][j].setNeuronVector(inputs);
                } else { //hidden layer and output
                    double layerSum = 0;
                    for(int k = 0; k < neurons[i - 1].length; k++){
                        layerSum += neurons[i - 1][k].fire(j, inputs);
                    }
                    neurons[i][j].setNeuronValue(layerSum);
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

    @Override
    public void reset() {
        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){
                neurons[i][j].reset();
            }
        }
    }

    private static final Font FONT = Font.font("serif", FontWeight.BOLD, FontPosture.REGULAR, 25);
    @Override
    protected void connectNeurons(GraphicsContext gc, Neuron n1, Neuron n2){
        double offset = Neuron.radius / 2;
        Point2D p1 = n1.getPos();
        Point2D p2 = n2.getPos();gc.setFont(FONT);
        gc.fillText("w "+ String.format("%.4f",n1.getWeight(n2.indexInLayer)), (p1.getX()+p2.getX()+2*offset)/2, (p1.getY()+p2.getY()+2*offset)/2);
        gc.strokeLine(p1.getX() + offset, p1.getY() + offset, p2.getX() + offset, p2.getY() + offset);
    }

}

package network;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class CPNetwork extends Network{

    public Neuron[][] neurons;

    public CPNetwork(int[] layersDesk) {

        super(layersDesk);

        this.neurons = new Neuron[layersDesk.length][];
        for(int i = 0; i < neurons.length; i++){
            neurons[i] = new Neuron[layersDesk[i]];
        }

        for (int i = 0; i < layersDesk.length; i++) {
            for(int j = 0; j < layersDesk[i]; j++) {
                if(i == 0) neurons[i][j] = new Neuron(j, layersDesk[i + 1]);
                if(i == 1) neurons[i][j] = new KohonenNeuron(j, layersDesk[i + 1]);
                if(i == 2) neurons[i][j] = new Instar(j,0);
                //if(i == 3) neurons[i][j] = new Neuron(j, 0);
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

        KohonenNeuron winner = (KohonenNeuron) neurons[1][0];
        double maxSum = 0;
        for(int i = 0; i < neurons.length; i++){

            if(i == 2) {
                winner.setNeuronValue(1);
                winner.setNeuronVector(input);
            }

            for(int j = 0; j < neurons[i].length; j++){

                if (i == 0)  { //input layer
                    neurons[i][j].setNeuronValue(input[j]);
                } else if (i == 1){ //Kohonen layer
                    double sum = 0;
                    for(int k = 0; k < neurons[0].length; k++){
                        sum += neurons[0][k].fire(j);
                    }
                    if(j == 0 || sum > maxSum){
                        maxSum = sum;
                        winner.setNeuronVector(new double[]{0});
                        winner = (KohonenNeuron) neurons[1][j];
                    }
                } else if (i == 2){
                    double layerSum = 0;
                    for(int k = 0; k < neurons[1].length; k++){
                        layerSum += neurons[1][k].fire(j);
                    }
                    neurons[i][j].activate(layerSum);
                } else if (i == 3){
                    double layerSum = 0;
                    for(int k = 0; k < neurons[i - 1].length; k++){
                        layerSum += neurons[i - 1][k].fire(j);
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

    private static final Font FONT = Font.font("serif", FontWeight.BOLD, FontPosture.REGULAR, 25);
    @Override
    protected void connectNeurons(GraphicsContext gc, Neuron n1, Neuron n2){
        double offset = Neuron.radius / 2;
        Point2D p1 = n1.getPos();
        Point2D p2 = n2.getPos();gc.setFont(FONT);
        gc.fillText("w " + String.format("%.4f", n1.getWeight(n2.indexInLayer)), (p1.getX() + p2.getX() + 2 * offset) / 2, (p1.getY() + p2.getY() + 2 * offset) / 2);
        gc.strokeLine(p1.getX() + offset, p1.getY() + offset, p2.getX() + offset, p2.getY() + offset);
    }

}

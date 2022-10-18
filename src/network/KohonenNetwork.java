package network;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class KohonenNetwork extends Network{

    public Neuron[][] neurons;

    public KohonenNetwork(int[] layersDesk) {

        super(layersDesk);

        this.neurons = new Neuron[layersDesk.length][];
        for(int i = 0; i < layersDesk.length; i++){
            neurons[i] = new Neuron[layersDesk[i]];
        }

        for (int i = 0; i < layersDesk.length; i++) {
            for(int j = 0; j < layersDesk[i]; j++) {
                if (i == 0) neurons[i][j] = new Neuron(j, layersDesk[i + 1]);
                else        neurons[i][j] = new KohonenNeuron(j,0);
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
        //Update input layers weight
        KohonenNeuron winner = (KohonenNeuron) neurons[1][0];
        double maxSum = 0;
        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){

                if (i == 0)  { //input layer
                    neurons[i][j].setNeuronValue(input[j]);
                } else { //Kohonen layer
                    double sum = 0;
                    for(int k = 0; k < neurons[0].length; k++){
                        sum += neurons[0][k].fire(j);
                    }
                    neurons[i][j].setNeuronValue(sum);

                    if(j == 0 || sum > maxSum){
                        maxSum = sum;
                        winner.setNeuronVector(new double[]{0});
                        winner = (KohonenNeuron) neurons[1][j];
                    }

                }

            }
        }

        //Cluster input vector
        winner.setNeuronVector(input);

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
        Point2D p2 = n2.getPos();
        gc.setFont(FONT);
        gc.fillText("w "+ String.format("%.4f",n1.getWeight(n2.indexInLayer)), (p1.getX()+p2.getX()+2*offset)/2, (p1.getY()+p2.getY()+2*offset)/2);
        gc.strokeLine(p1.getX() + offset, p1.getY() + offset, p2.getX() + offset, p2.getY() + offset);
    }

}

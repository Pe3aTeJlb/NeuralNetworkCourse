package network;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class KohonenNetwork extends Network{

    public KohonenNeuron[][] neurons;

    public KohonenNetwork(int[] layersDesk) {

        super(layersDesk);

        this.neurons = new KohonenNeuron[layersDesk.length][];
        for(int i = 0; i < layersDesk.length; i++){
            neurons[i] = new KohonenNeuron[layersDesk[i]];
        }

        for (int i = 0; i < layersDesk.length; i++) {
            for(int j = 0; j < layersDesk[i]; j++) {
                if (i == 0) neurons[i][j] = new KohonenNeuron(j, layersDesk[i + 1]);
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
        //Update input layers weight
        KohonenNeuron winner = neurons[1][0];
        double minSum = 0;
        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){

                if (i == 0)  { //input layer
                    neurons[i][j].setNeuronValue(input[j]);
                } else { //Kohonen layer
                    double sum = 0;
                    for(int k = 0; k < neurons[0].length; k++){
                        sum += Math.pow(neurons[0][k].getNeuronValue() - neurons[0][k].getWeight(j), 2);
                    }
                    sum = Math.sqrt(sum);
                    if(j == 0 || sum < minSum){
                        minSum = sum;
                        winner.setNeuronVector(new double[]{0});
                        winner = neurons[1][j];
                    }

                }

            }
        }

        //Cluster input vector
        winner.setNeuronVector(input);
        return new double[]{winner.indexInLayer};
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

package networks;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public abstract class Network {

    public double layerStep = Neuron.radius + 200;
    public double neuronStep = Neuron.radius + 100;

    public int inputSize;
    public final Neuron[][] neurons;

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

    public abstract void train(String path);

    public abstract void simulate(String input);

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

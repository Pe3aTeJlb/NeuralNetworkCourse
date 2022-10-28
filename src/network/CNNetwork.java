package network;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.Arrays;

public class CNNetwork extends Network{

    public CNNDefNeuron[][] neurons;

    public CNNetwork(int[] layersDesk) {

        super(layersDesk);

        this.neurons = new CNNDefNeuron[layersDesk.length][];
        for(int i = 0; i < layersDesk.length; i++){
            neurons[i] = new CNNDefNeuron[layersDesk[i]];
        }

        for (int i = 0; i < layersDesk.length; i++) {
            for(int j = 0; j < layersDesk[i]; j++) {
                if (i < layersDesk.length - 1) neurons[i][j] = new CNNDefNeuron(j, layersDesk[i + 1]);
                else                           neurons[i][j] = new CNNDefNeuron(j,0);
            }
        }

        ((Network) this).neurons = neurons;

    }


    @Override
    public double[] simulate(String[] input){
        String[] in = input[0].split(",");
        double[] data = new double[in.length];
        for(int i = 0; i < in.length; i++){
            data[i] = Double.parseDouble(in[i]);
        }
        return simulate(data);
    }

    public double[] simTest(ArrayList<Double> input){

        clearNeurons();

        //Update input layers weight
        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){

                if (i == 0)  { //input layer
                    neurons[i][j].setNeuronValue(input.get(j));
                } else { //hidden layer and output
                    double layerSum = 0;
                    for(int k = 0; k < neurons[i - 1].length; k++){
                        layerSum += neurons[i - 1][k].fire(j);
                    }
                    //System.out.println("laysum " + layerSum);
                    neurons[i][j].activate(layerSum);
                }

            }
        }

        //Output layer to vector
        double[] result = new double[outputSize];
        for(int i = 0; i < outputSize; i++){
            result[i] = neurons[neurons.length - 1][i].getNeuronValue();
        }
        System.out.println(Arrays.toString(result));

        return result;

    }

    @Override
    public double[] simulate(double[] input){

        clearNeurons();

        ArrayList<Double> convolution = convolve(input);

        //Update input layers weight
        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){

                if (i == 0)  { //input layer
                    neurons[i][j].setNeuronValue(convolution.get(j));
                } else { //hidden layer and output
                    double layerSum = 0;
                    for(int k = 0; k < neurons[i - 1].length; k++){
                        layerSum += neurons[i - 1][k].fire(j);
                    }
                    //System.out.println("laysum " + layerSum);
                    neurons[i][j].activate(layerSum);
                }

            }
        }

        //Output layer to vector
        double[] result = new double[outputSize];
        for(int i = 0; i < outputSize; i++){
            result[i] = neurons[neurons.length - 1][i].getNeuronValue();
        }
        System.out.println(Arrays.toString(result));

        return result;

    }


    private double[][] verticalFilter = new double[][]{{1, 0, -1}, {1, 0, -1}, {1, 0, -1}};
    private double[][] horizontalFilter = new double[][]{{1, 1, 1}, {0, 0, 0}, {-1, -1, -1}};
    private ArrayList<double[][]> filters = new ArrayList<>(Arrays.asList(verticalFilter, horizontalFilter));
    private ArrayList<double[][]> convolutions = new ArrayList<>();
    private ArrayList<double[][]> maxPools = new ArrayList<>();
    private int poolSize = 2;
    private ArrayList<Double> flatter = new ArrayList<>();

    private ArrayList<Double> convolve(double[] input){

        double[][] data = new double[28][28];

        for(int i = 0; i < input.length / 28; i++){
            for(int j = 0; j < 28; j++){
                data[i][j] = (input[i*28+j]);
            }
        }

        for(int i = 0; i < data.length; i++){
            System.out.println(Arrays.toString(data[i]));
        }
        System.out.println("\n");

        for(double[][] filter: filters){

            double[][] convolution = new double[data.length - 2][data.length - 2];

            for(int i = 0; i < convolution.length; i++){
                for(int j = 0; j < convolution[i].length; j++){

                    double cell = 0;
                    //for filter height
                    for(int k = i; k < i + filter.length; k++){
                        for(int m = j; m < j + filter.length; m++){
                            // System.out.print(data[k][m] + "(" +k+" "+m+")"+" * " + filter[k - i][m - j] + " | ");
                            cell += data[k][m] * filter[k - i][m - j];
                        }
                        // System.out.println(" ");
                    }
                    convolution[i][j] = cell;
                    // System.out.println("cell "+ i+ " " +j  + " is " + cell+ "\n");

                }
                //System.out.println("\n");
            }


            //ReLu
            for(int i = 0; i < convolution.length; i++){
                for(int j = 0; j < convolution[i].length; j++){
                    convolution[i][j] = Math.max(0, convolution[i][j]);
                    //System.out.print(convolution[i][j] + " ");
                }
                //System.out.println("");
            }
            //System.out.println("\n\n");

            convolutions.add(convolution);

            //Max pool
            double[][] pool = new double[convolution.length - 1][convolution.length - 1];

            for(int i = 0; i < pool.length; i++){
                for(int j = 0; j < pool[i].length; j++){

                    double maxCell = 0;
                    //for filter height
                    for(int k = i; k < i + poolSize; k++){
                        for(int m = j; m < j + poolSize; m++){
                            //System.out.print(convolution[k][j] + "(" +k+" "+m+")" + " | ");
                            if(maxCell < convolution[k][m]) maxCell = convolution[k][m];
                        }
                        //System.out.println(" ");
                    }
                    pool[i][j] = maxCell;
                    flatter.add(maxCell);
                    //System.out.println("maxCell "+ i+ " " +j  + " is " + maxCell+ "\n");

                }
                //System.out.println("\n");
            }


            for(int i = 0; i < pool.length; i++){
                for(int j = 0; j < pool[i].length; j++){
                    pool[i][j] = Math.max(0, pool[i][j]);
                    // System.out.print(pool[i][j] + " ");
                }
                // System.out.println("");
            }

            maxPools.add(pool);

        }

        return flatter;

    }

    private static final Font FONT = Font.font("serif", FontWeight.BOLD, FontPosture.REGULAR, 25);
    @Override
    protected void connectNeurons(GraphicsContext gc, Neuron n1, Neuron n2){
        /*
        double offset = Neuron.radius / 2;
        Point2D p1 = n1.getPos();
        Point2D p2 = n2.getPos();gc.setFont(FONT);
        gc.fillText("w "+ String.format("%.4f",n1.getWeight(n2.indexInLayer)), (p1.getX()+p2.getX()+2*offset)/2, (p1.getY()+p2.getY()+2*offset)/2);
        gc.strokeLine(p1.getX() + offset, p1.getY() + offset, p2.getX() + offset, p2.getY() + offset);

         */
    }

}

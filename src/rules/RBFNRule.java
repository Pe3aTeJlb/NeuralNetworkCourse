package rules;

import network.Network;
import network.RBFNetwork;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class RBFNRule extends Rule{

    private RBFNetwork net;

    public RBFNRule(int[] inputNeuronsCount){
        this.net = new RBFNetwork(inputNeuronsCount);
        net.setRule(this);
    }

    @Override
    public void setNetwork(Network network) {
        net = (RBFNetwork) network;
        net.setRule(this);
    }

    @Override
    public Network getNetwork() {
        return net;
    }

    public void train(String dataset) {
/*
        net.reset();

        ArrayList<double[]> inputs = new ArrayList<>();
        ArrayList<double[]> outputs = new ArrayList<>();
        ArrayList<double[]> centers = new ArrayList<>();

        InputStream inputStream = HebbRule.class.getResourceAsStream(dataset);
        Scanner reader = new Scanner(inputStream);
        while (reader.hasNextLine()) {

            String line = reader.nextLine();
            if(line.equals("")) continue;

            //input input to double vector
            String[] buff =  line.trim().split(" ");
            double[] data = new double[buff.length];
            for(int i = 0; i < buff.length; i++){
                data[i] = Double.parseDouble(buff[i]);
            }

            outputs.add(data.)
            if(Double.parseDouble(data[data.length-1]) == 1){
                centers.add(Arrays.copyOfRange(input, 0, input.length-1));
            }

        }

        InputStream inputStream = HebbRule.class.getResourceAsStream(dataset);
        Scanner reader = new Scanner(inputStream);
        ArrayList<double[]> inputs = new ArrayList<>();
        ArrayList<double[]> centers = new ArrayList<>();

        while (reader.hasNextLine()) {

            String line = reader.nextLine();
            if(line.equals("")) continue;

            String[] data =  line.trim().split(" ");
            System.out.println("input: " + line);

            double[] input = new double[data.length];
            for(int i = 0; i < data.length; i++){
                input[i] = Double.parseDouble(data[i]);
            }
            inputs.add(input);
            if(Double.parseDouble(data[data.length-1]) == 1){
                centers.add(Arrays.copyOfRange(input, 0, input.length-1));
            }

        }

        //set centers
        for(int i = 0; i < centers.size(); i++){
            net.neurons[0][i].setCenter(centers.get(i));
        }

        //Train network

        for(int i = 0; i < inputs.size(); i++) {

            double netOutput = 0;
            for (int j = 0; j < inputs.get(i).length - 1; j++){
                netOutput += net.neurons[0][j].fire(0, inputs.get(i));
            }

            for (int j = 0; j < inputs.get(i).length - 1; j++){
                net.neurons[0][j].updateWeight(0, inputs.get(i), inputs.get(i)[inputs.get(i).length-1], netOutput);
            }

        }
*/
    }

    @Override
    public String toString() {
        return "RBF";
    }

}

class rbfn {
    public static void main(String a[]){
        Net n=new rbfn().new Net();
        for(int i=0;i<100;i++){
            n.train();
        }
        n.test(new double[]{0,0});
        n.test(new double[]{0,1});
        n.test(new double[]{1,0});
        n.test(new double[]{1,1});


    }

    class Net{
        double[][] inputs=new double[][]{{0,0},{0,1},{1,0},{1,1}};
        double[] outputs= {1,0,0,1};
        Unit[] net =new Unit[2];

        Net(){
            net[0]=new Unit(0.5,new double[]{0,0},0.5);
            net[1]=new Unit(0.5,new double[]{1,1},0.5);
        }

        void train(){
            for(int i=0;i<inputs.length;i++){
                double output=outputs[i];
                double predictedoutput=0;
                for(int j=0;j<inputs[i].length;j++){
                    predictedoutput+=net[j].phi(inputs[i])*net[j].w;

                }
                //predictedoutput= Math.round(predictedoutput);

                for(int j=0;j<inputs[i].length;j++){
                    net[j].update(inputs[i], output, predictedoutput);
                }
            }
        }

        void test(double[] inputs){
            double predictedOutput=0;
            for(int i=0;i<inputs.length;i++){
                predictedOutput+=net[i].phi(inputs)*net[i].w;
                System.out.print("| " + net[i].w +"\t"+net[i].c[0]+"\t"+net[i].c[1]+"\t");
            }
            System.out.println();
            for(int i=0;i<inputs.length;i++){
                System.out.print("- " +inputs[i]+"\t");
            }
            System.out.print(predictedOutput);
            System.out.println();

        }


        class Unit{
            double w;
            double c[];
            double sigma;
            double n1=0.1;
            double n2=0.1;
            Unit(double sigma,double[] center,double weight){
                this.sigma=sigma;
                this.c=center;
                this.w=weight;

            }

            double phi(double[] input){
                double distance=0;
                for(int i=0;i<c.length;i++)
                    distance+=Math.pow(input[i]-c[i],2);
                return Math.pow(Math.E,- distance/(2*Math.pow(sigma, 2)));
            }

            void update(double[] input,double desired,double output){
                double phi=phi(input);
                double diffOutput=desired-output;

                for(int i=0;i<c.length;i++)
                    c[i]=c[i]+ (n1*diffOutput*w* phi*(input[i]-c[i])/(sigma*sigma));


                w=w+(n2*diffOutput*phi);
            }
        }
    }
}

/*
* 'multiquadric': lambda x: np.sqrt(x**2 + 1),
    'inverse':      lambda x: 1.0 / np.sqrt(x**2 + 1),
    'gaussian':     lambda x: np.exp(-x**2),
    'linear':       lambda x: x,
    'quadric':      lambda x: x**2,
    'cubic':        lambda x: x**3,
    'quartic':      lambda x: x**4,
    'quintic':      lambda x: x**5,
    'thin_plate':   lambda x: x**2 * np.log(x + 1e-10),
    'logistic':     lambda x: 1.0 / (1.0 + np.exp(-np.clip(x, -5, 5))),
    'smoothstep':   lambda x: ((np.clip(1.0 - x, 0.0, 1.0))**2.0) * (3 - 2*(np.clip(1.0 - x, 0.0, 1.0)))
}*/
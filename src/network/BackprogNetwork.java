package network;

public class BackprogNetwork extends Network{

    private double bias;
    public SimpleNeuron[][] neurons;

    public BackprogNetwork(int[] inputNeuronsCount){

        super(inputNeuronsCount);

        this.neurons = new SimpleNeuron[inputNeuronsCount.length][];
        for(int i = 0; i < inputNeuronsCount.length; i++){
            neurons[i] = new SimpleNeuron[inputNeuronsCount[i]];
        }

        for (int i = 0; i < inputNeuronsCount.length; i++) {
            for(int j = 0; j < inputNeuronsCount[i]; j++) {
                neurons[i][j] = new SimpleNeuron();
            }
        }

        ((Network) this).neurons = neurons;

    }

    @Override
    public double simulate(String[] input){

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
        System.out.println("new bias " + getBias());
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    @Override
    public void reset(){
        bias = 0;
        for(int i = 0; i < neurons.length; i++){
            for(int j = 0; j < neurons[i].length; j++){
                neurons[i][j].reset();
            }
        }
    }

}

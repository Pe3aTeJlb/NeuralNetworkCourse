package network;

public class BackprogNeuron extends Neuron{


    @Override
    public void updateWeight(double addWeight){
        this.weight += addWeight;
        System.out.println("new weight " + weight);
    }

    public double getWeight() {
        return this.weight;
    }

    public double fire(double inputValue) {
        neuronValue = inputValue;
        return inputValue * weight;
    }

    @Override
    public void reset(){
        neuronValue = 0;
        weight = 0;
    }


    public void setNeuronValue(int neuronValue){
        this.neuronValue = neuronValue;
    }

    public double getNeuronValue(){return neuronValue;}

}

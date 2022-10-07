package Rules;

import network.Network;

public abstract class Rule {

    public Network net;

    public abstract void setNetwork(Network network);

    public abstract void train(String dataset);

    @Override
    public abstract String toString();

}
/*
*             /*
            int sim = simulate(data);
            if(sim != Integer.parseInt(data[2])){

                if(sim == 0){
                    System.out.println("increase");
                    for(int i = 0; i < neurons[0].length; i++){
                        neurons[0][i].increaseWeight(Integer.parseInt(data[i]));
                    }
                } else {
                    System.out.println("decrease");
                    for(int i = 0; i < neurons[0].length; i++){
                        neurons[0][i].decreaseWeight(Integer.parseInt(data[i]));
                    }
                }

            }

            System.out.println("bias " + getBias());
            updateBias(sim);


             */
package BackpackProblem.Sequential;

import BackpackProblem.Sequential.Evolution.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Population {
    public static final int COUNT_OF_POPULATIONS = 500;
    public static final int MUTATION_FACTOR = 10; // in range 0 to 100
    public static final int CROSSING_FACTOR = 50; // in range 1 to COUNT_OF_POPULATION
    public static final int CAPACITY = 2500; // max weight of backpack
    private static final int STOP_CONDITION = 100; // count of the same results in a row

    public List<Item> items;
    public Boolean[][] currentPopulation;
    public Crossover cross;
    public Mutation mutation;
    public LocalImprovement improvement;
    private int sameCostWeightCount = 0;
    private int lastCost = 0;
    private int lastWeight = 0;

    private int iteration = 0;
    public Population(List<Item> items) {
        this.items = items;
        this.currentPopulation = new Boolean[COUNT_OF_POPULATIONS][Item.COUNT_OF_ITEMS];
        cross = new Crossover(this);
        mutation = new Mutation(this);
        improvement = new LocalImprovement(this);
    }
    public void start() {
        initPopulation();
        while (sameCostWeightCount < STOP_CONDITION){
            int indexOfSetMaxCost = Utils.findSetWithMaxCost(this);

            int random;
            do {
                random = (int) (Math.random() * (Population.COUNT_OF_POPULATIONS));
            } while (random == indexOfSetMaxCost);
            Boolean[] parent1 = this.currentPopulation[indexOfSetMaxCost];
            Boolean[] parent2 = this.currentPopulation[random];

            EvolutionThread evolutionThread = new EvolutionThread(this, parent1, parent2, true);
            evolutionThread.start();
            EvolutionThread evolutionThread2 = new EvolutionThread(this, parent1, parent2, false);
            evolutionThread2.start();

            try {
                evolutionThread.join();
                evolutionThread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            checkResult();
            iteration++;
            /*if (iteration % 100 == 0) {
                System.out.println(iteration + "\t\t\t" + lastWeight + "\t\t\t" + lastCost);
            }*/
        }
        /*
        System.out.println("Result: ");
        System.out.println("Iteration: " + iteration);
        System.out.println("Weight: " + lastWeight);
        System.out.println("Cost: " + lastCost);
         */
    }
    private void initPopulation(){
        for (int i = 0; i < COUNT_OF_POPULATIONS; i++) {
            for (int j = 0; j < Item.COUNT_OF_ITEMS; j++) {
                currentPopulation[i][j] = i == j;
            }
        }
    }

    private void checkResult(){
        int indexOfSetMaxCost = Utils.findSetWithMaxCost(this);

        int newMaxCost = Utils.calculateCostOfSet(this, currentPopulation[indexOfSetMaxCost]);
        int newWeight = Utils.calculateWeightOfSet(this, currentPopulation[indexOfSetMaxCost]);
        if(newMaxCost == lastCost && newWeight == lastWeight){
            sameCostWeightCount++;
        }
        else {
            sameCostWeightCount = 0;
            lastCost = newMaxCost;
            lastWeight = newWeight;
        }
    }

}

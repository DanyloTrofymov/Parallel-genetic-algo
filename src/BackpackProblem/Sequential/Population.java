package BackpackProblem.Sequential;

import BackpackProblem.Sequential.Evolution.Crossover;
import BackpackProblem.Sequential.Evolution.LocalImprovement;
import BackpackProblem.Sequential.Evolution.Mutation;
import BackpackProblem.Sequential.Evolution.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Population {
    public static final int COUNT_OF_POPULATIONS = 100;
    public static final int MUTATION_FACTOR = 10; // in range 0 to 100
    public static final int CROSSING_FACTOR = 50; // in range 1 to COUNT_OF_POPULATION
    public static final int CAPACITY = 250; // max weight of backpack
    private static final int STOP_CONDITION = 100; // count of the same results in a row

    public List<Item> items;
    public Boolean[][] currentPopulation;
    int sameCostWeightCount = 0;
    int lastCost = 0;
    int lastWeight = 0;

    int iteration = 0;
    public Population(List<Item> items) {
        this.items = items;
        this.currentPopulation = new Boolean[COUNT_OF_POPULATIONS][Item.COUNT_OF_ITEMS];
    }
    public void start() {
        System.out.println("Iteration \t weight \t cost");
        Crossover cross = new Crossover(this);
        Mutation mutation = new Mutation(this);
        LocalImprovement improvement = new LocalImprovement(this);
        initPopulation();
        while (sameCostWeightCount < STOP_CONDITION){
            List<Integer> childrenIndexes= new ArrayList<>();
            int indexOfSetMaxCost = (int) (Math.random() * (Population.COUNT_OF_POPULATIONS)); //Utils.findSetWithMaxCost(population);

            int random;
            do{
                random = (int) (Math.random() * (Population.COUNT_OF_POPULATIONS));
            }while (random == indexOfSetMaxCost);
            Boolean[] parent1 = currentPopulation[indexOfSetMaxCost];
            Boolean[] parent2 = currentPopulation[random];

            int index = cross.crossover(parent1, parent2, true);
            if(index != -1){
                childrenIndexes.add(index);
            }
            index = cross.crossover(parent1, parent2, false);
            if(index != -1){
                childrenIndexes.add(index);
            }

            for (Integer childrenIndex : childrenIndexes) {
                mutation.mutate(childrenIndex);
                improvement.improve(childrenIndex);
            }
            checkResult();
            iteration++;
            if (iteration % 100 == 0) {
                System.out.println(iteration + "\t\t\t" + lastWeight + "\t\t\t" + lastCost);
            }
        }
        System.out.println("Result: ");
        System.out.println("Iteration: " + iteration);
        System.out.println("Weight: " + lastWeight);
        System.out.println("Cost: " + lastCost);
    }
    private void initPopulation(){
        for (int i = 0; i < Item.COUNT_OF_ITEMS; i++) {
            for (int j = 0; j < COUNT_OF_POPULATIONS; j++) {
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

package BackpackProblem.Sequential;

import BackpackProblem.Sequential.Evolution.Crossover;
import BackpackProblem.Sequential.Evolution.LocalImprovement;
import BackpackProblem.Sequential.Evolution.Mutation;
import BackpackProblem.Sequential.Evolution.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class Population {
    public static int COUNT_OF_POPULATIONS = 500;
    public static final int MUTATION_FACTOR = 10; // in range 0 to 100
    public static final int CROSSING_FACTOR = 50; // in range 1 to COUNT_OF_POPULATION
    public static int CAPACITY = 250; // max weight of backpack
    private static final int STOP_CONDITION = 10000; // number of total iteration

    private Crossover cross;
    private Mutation mutation;
    private LocalImprovement improvement;

    public List<Item> items;
    public Boolean[][] currentPopulation;
    private int sameCostWeightCount = 0;
    private int lastCost = 0;
    private int lastWeight = 0;

    int iteration = 0;
    public Population(List<Item> items) {
        this.items = items;
        this.currentPopulation = new Boolean[COUNT_OF_POPULATIONS][Item.COUNT_OF_ITEMS];
        cross = new Crossover(this);
        mutation = new Mutation(this);
        improvement = new LocalImprovement(this);
    }
    public void start() {
        //System.out.println("Iteration \t weight \t cost");
        initPopulation();
        while (iteration < STOP_CONDITION){
            int indexOfSetMaxCost = Utils.findSetWithMaxCost(this);

            int random;
            do{
                random = (int) (Math.random() * (Population.COUNT_OF_POPULATIONS));
            }while (random == indexOfSetMaxCost);
            Boolean[] parent1 = currentPopulation[indexOfSetMaxCost];
            Boolean[] parent2 = currentPopulation[random];

            Boolean[] child1 = cross.getChild(parent1, parent2);
            child1 = mutation.mutate(child1);
            child1 = improvement.improve(child1);

            int child1Weight = Utils.calculateWeightOfSet(this, child1);
            int indexOfSetLessWeight = Utils.findSetWithMinWeight(this);

            if(canInsert(child1Weight, indexOfSetLessWeight)){
                setPopulation(child1, indexOfSetLessWeight);
            }

            Boolean[] child2 = cross.getChild(parent2, parent1);
            child2 = mutation.mutate(child2);
            child2 = improvement.improve(child2);

            int child2Weight = Utils.calculateWeightOfSet(this, child2);
            indexOfSetLessWeight = Utils.findSetWithMinWeight(this);

            if(canInsert(child2Weight, indexOfSetLessWeight)){
                setPopulation(child2, indexOfSetLessWeight);
            }

            checkResult();
            iteration++;
            /*if (iteration % 100 == 0) {
                System.out.println(iteration + "\t\t\t" + lastWeight + "\t\t\t" + lastCost);
            }
             */
        }
        /*System.out.println("Result: ");
        System.out.println("Iteration: " + iteration);
        System.out.println("Weight: " + lastWeight);
        System.out.println("Cost: " + lastCost);*/
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

    private void setPopulation(Boolean[] set, int indexOfSet){
        System.arraycopy(set, 0, this.currentPopulation[indexOfSet], 0, Item.COUNT_OF_ITEMS);
    }

    private boolean canInsert(int currentWeight, int lessWeightIndex){
        if(currentWeight <= Population.CAPACITY && currentWeight >= Utils.calculateWeightOfSet(this, this.currentPopulation[lessWeightIndex])){
            return true;
        }
        return false;
    }
    public void writeToFile(String fileName) {
        try {
            Boolean[] bestSet = currentPopulation[Utils.findSetWithMaxCost(this)];
            PrintWriter printWriter = new PrintWriter(new File(fileName));
            printWriter.println("Weight: " + lastWeight);
            printWriter.println("Cost: " + lastCost);
            for (int i = 0; i < bestSet.length; i++) {
                if(bestSet[i]) {
                    printWriter.println(items.get(i));
                }
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

package BackpackProblem.Sequential;

import BackpackProblem.Sequential.Evolution.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Population {
    public static final int COUNT_OF_POPULATIONS = 500;
    public static final int MUTATION_FACTOR = 10; // in range 0 to 100
    public static final int CROSSING_FACTOR = 50; // in range 1 to COUNT_OF_POPULATION
    public static final int CAPACITY = 2500; // max weight of backpack
    private static final int STOP_CONDITION = 10000; // total iterations

    public List<Item> items;
    public Boolean[][] currentPopulation;
    public Crossover cross;
    public Mutation mutation;
    public LocalImprovement improvement;
    private int sameCostWeightCount = 0;
    private int lastCost = 0;
    private int lastWeight = 0;
    public int numThreads = 8;
    private int iteration = 0;
    public Population(List<Item> items) {
        this.items = items;
        this.currentPopulation = new Boolean[COUNT_OF_POPULATIONS][Item.COUNT_OF_ITEMS];
        cross = new Crossover(this);
        mutation = new Mutation(this);
        improvement = new LocalImprovement(this);
    }
    public Population(List<Item> items, Boolean[][] currentPopulation) {
        this.items = items;
        this.currentPopulation = currentPopulation;
        cross = new Crossover(this);
        mutation = new Mutation(this);
        improvement = new LocalImprovement(this);
    }
    public void start() {
        initPopulation();
        int subPopulationSize = COUNT_OF_POPULATIONS / numThreads;
        List<Boolean[][]> subPopulations = getSubsets(currentPopulation, subPopulationSize);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Callable<Object>> todo = new ArrayList<>();
        while (iteration < STOP_CONDITION){
            for (int i = 0; i < numThreads; i++) {
                Thread thread = new Thread(new EvolutionThread(new Population(this.items, subPopulations.get(i))));
                todo.add(Executors.callable(thread));
            }
            try {
                executorService.invokeAll(todo);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            checkResult();
            iteration += EvolutionThread.MIGRATION_FACTOR * numThreads;

            for (int i = 0; i < numThreads; i++) {
                System.arraycopy(subPopulations.get(i), 0, currentPopulation, i * subPopulationSize, subPopulationSize);
            }

            Utils.shuffle(currentPopulation);

            /*if (iteration % 100 == 0) {
                System.out.println(iteration + "\t\t\t" + lastWeight + "\t\t\t" + lastCost);
            }*/
        }
        //System.out.println("lastCost: " + lastCost +  " lastWeight: " + lastWeight);
        executorService.shutdown();
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

    private List<Boolean[][]> getSubsets(Boolean[][] currentPopulation, int partsCount){

        List<Boolean[][]> subPopulations = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            Boolean[][] subPopulation = Arrays.copyOfRange(currentPopulation, i * partsCount, (i + 1) * partsCount);
            subPopulations.add(subPopulation);
        }
        return subPopulations;
    }

}

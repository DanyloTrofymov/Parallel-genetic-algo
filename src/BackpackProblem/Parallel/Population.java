package BackpackProblem.Parallel;

import BackpackProblem.Parallel.Evolution.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Population {
    public static int COUNT_OF_POPULATIONS = 500;
    public static final int MUTATION_FACTOR = 10; // in range 0 to 100
    public static final int CROSSING_FACTOR = 50; // in range 1 to COUNT_OF_POPULATION
    public static int CAPACITY = 2500; // max weight of backpack
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
    public void start(double cofficientOfSubpopulation) {
        initPopulation();
        int countOfSubpopulations = (int) (numThreads * cofficientOfSubpopulation);
        int subPopulationSize = COUNT_OF_POPULATIONS / countOfSubpopulations;
        List<Boolean[][]> subPopulations = getSubpopulations(currentPopulation, subPopulationSize, countOfSubpopulations);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Callable<Object>> todo = new ArrayList<>();
        while (iteration < STOP_CONDITION){
            for (int i = 0; i < countOfSubpopulations; i++) {
                Thread thread = new Thread(new EvolutionThread(new Population(this.items, subPopulations.get(i))));
                todo.add(Executors.callable(thread));
            }
            try {
                executorService.invokeAll(todo);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            checkResult();
            iteration += EvolutionThread.MIGRATION_FACTOR * countOfSubpopulations;

            for (int i = 0; i < countOfSubpopulations; i++) {
                System.arraycopy(subPopulations.get(i), 0, currentPopulation, i * subPopulationSize, subPopulationSize);
            }

            Utils.shuffle(currentPopulation);

            /*if (iteration % 100 == 0) {
                System.out.println(iteration + "\t\t\t" + lastWeight + "\t\t\t" + lastCost);
            }*/
        }
        //System.out.println("lastCost: " + lastCost +  " lastWeight: " + lastWeight);
        executorService.shutdown();

        System.out.println("Result: ");
        //System.out.println("Iteration: " + iteration);
        System.out.println("Weight: " + lastWeight);
        System.out.println("Cost: " + lastCost);

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

    private List<Boolean[][]> getSubpopulations(Boolean[][] currentPopulation, int partsSize, int countOfSubpopulations){

        List<Boolean[][]> subPopulations = new ArrayList<>();
        for (int i = 0; i < countOfSubpopulations; i++) {
            Boolean[][] subPopulation = Arrays.copyOfRange(currentPopulation, i * partsSize, (i + 1) * partsSize);
            subPopulations.add(subPopulation);
        }
        return subPopulations;
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

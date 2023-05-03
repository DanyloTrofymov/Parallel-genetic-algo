package BackpackProblem.Parallel;

import BackpackProblem.Parallel.Evolution.EvolutionThread;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        int countOfItems = 50;
        int countOfPopulations= 50;
        int backpackCapacity = 25;

        List<Integer> coefficient  = List.of(1, 5, 10, 15, 20);//List.of(1, 2, 3, 5, 7, 10, 15, 20);
        coefficient.forEach(k -> {
            startCoeficientTest(countOfItems*k, countOfPopulations*k, backpackCapacity*k);
        });
        List<Integer> numOfThreads  = List.of(4, 8, 16);
        List<Double> coeficientsOfPopulation = List.of(0.5, 1.0, 2.0, 4.0);
        List<Integer> migrationFactors  = List.of(5, 10, 20, 50);

        /*numOfThreads.forEach(k -> {
            startNumOfThreadsTest(k);
        });*/
        /*coeficientsOfPopulation.forEach(k -> {
            startCoeficientOfPopulationsTest(k);
        });*/
        /*migrationFactors.forEach(k -> {
            startMigrationFactorTest(k);
        });*/

    }
    private static void startCoeficientTest(int countOfItems, int countOfPopulations, int backpackCapacity){
        Item.COUNT_OF_ITEMS=countOfItems;
        Population.COUNT_OF_POPULATIONS=countOfPopulations;
        Population.CAPACITY=backpackCapacity;

        var items = Item.generateItems();
        System.out.println("Count of population: " + Population.COUNT_OF_POPULATIONS +
                "\nCount of items:" + Item.COUNT_OF_ITEMS + "\nBackpack capacity: " + Population.CAPACITY);
        for (int i = 0; i < 20; i++){


            Population population = new Population(items);

            long startTime = System.currentTimeMillis();
            population.start(1);
            long endTime = System.currentTimeMillis();
            System.out.println((endTime - startTime));
        }
    }
    private static void startNumOfThreadsTest(int numOfThreads){
        Item.COUNT_OF_ITEMS=50*15;
        Population.COUNT_OF_POPULATIONS=50*15;
        Population.CAPACITY=25*15;

        var items = Item.generateItems();
        System.out.println("numOfThreads: " + numOfThreads);
        for (int i = 0; i < 20; i++){
            Population population = new Population(items);
            population.numThreads = numOfThreads;
            long startTime = System.currentTimeMillis();
            population.start(1);
            long endTime = System.currentTimeMillis();
            System.out.println((endTime - startTime));
        }
    }
    private static void startMigrationFactorTest(int migrationFactor){
        Item.COUNT_OF_ITEMS=50*15;
        Population.COUNT_OF_POPULATIONS=50*15;
        Population.CAPACITY=25*15;

        var items = Item.generateItems();
        System.out.println("Migration factor: " + migrationFactor);
        for (int i = 0; i < 20; i++){

            EvolutionThread.MIGRATION_FACTOR = migrationFactor;
            Population population = new Population(items);
            long startTime = System.currentTimeMillis();
            population.start(1);
            long endTime = System.currentTimeMillis();
            //System.out.println((endTime - startTime));
        }
    }
    private static void startCoeficientOfPopulationsTest(double coeficientOfPopulation){
        Item.COUNT_OF_ITEMS=50*15;
        Population.COUNT_OF_POPULATIONS=50*15;
        Population.CAPACITY=25*15;

        var items = Item.generateItems();
        System.out.println("Coeficient of population:" + coeficientOfPopulation);
        for (int i = 0; i < 20; i++){

            Population population = new Population(items);
            long startTime = System.currentTimeMillis();
            population.start(coeficientOfPopulation);
            long endTime = System.currentTimeMillis();
            System.out.println((endTime - startTime));
        }
    }
}
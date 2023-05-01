package BackpackProblem.Sequential.Evolution;

import BackpackProblem.Sequential.Item;
import BackpackProblem.Sequential.Population;


public class EvolutionThread extends Thread {

    private static final int MIGRATION_FACTOR = 10;
    Population population;
    public EvolutionThread (Population population) {
        this.population = population;
    }
    @Override
    public void run() {
        for (int i = 0; i < MIGRATION_FACTOR; i++) {
            int indexOfSetMaxCost = Utils.findSetWithMaxCost(population);

            int random;
            do {
                random = (int) (Math.random() * (Population.COUNT_OF_POPULATIONS));
            } while (random == indexOfSetMaxCost);
            Boolean[] parent1 = population.currentPopulation[indexOfSetMaxCost];
            Boolean[] parent2 = population.currentPopulation[random];

            Boolean[] child1 = population.cross.getChild(parent1, parent2);
            child1 = population.mutation.mutate(child1);
            child1 = population.improvement.improve(child1);

            int child1Weight = Utils.calculateWeightOfSet(population, child1);
            int indexOfSetLessWeight = Utils.findSetWithMinWeight(population);

            if (canInsert(child1Weight, indexOfSetLessWeight)) {
                setPopulation(child1, indexOfSetLessWeight);
            }

            Boolean[] child2 = population.cross.getChild(parent2, parent1);
            child2 = population.mutation.mutate(child2);
            child2 = population.improvement.improve(child2);

            int child2Weight = Utils.calculateWeightOfSet(population, child2);
            indexOfSetLessWeight = Utils.findSetWithMinWeight(population);

            if (canInsert(child2Weight, indexOfSetLessWeight)) {
                setPopulation(child2, indexOfSetLessWeight);
            }
        }

    }
    private synchronized void setPopulation(Boolean[] set, int indexOfSet){
        System.arraycopy(set, 0, population.currentPopulation[indexOfSet], 0, Item.COUNT_OF_ITEMS);
    }

    private boolean canInsert(int currentWeight, int lessWeightIndex){
        if(currentWeight <= Population.CAPACITY && currentWeight >= Utils.calculateWeightOfSet(population, population.currentPopulation[lessWeightIndex])){
            return true;
        }
        return false;
    }
}

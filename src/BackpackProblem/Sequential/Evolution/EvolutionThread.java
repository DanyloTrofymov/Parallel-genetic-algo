package BackpackProblem.Sequential.Evolution;

import BackpackProblem.Sequential.Item;
import BackpackProblem.Sequential.Population;

import javax.swing.plaf.ButtonUI;
import java.util.ArrayList;
import java.util.List;

public class EvolutionThread extends Thread {
    Population population;
    Boolean[] parent1;
    Boolean[] parent2;
    boolean direct;

    public EvolutionThread (Population population, Boolean[] parent1, Boolean[] parent2, boolean direct) {
        this.population = population;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.direct = direct;
    }
    @Override
    public void run() {
        Boolean[] child = population.cross.crossover(parent1, parent2, direct);
        child = population.mutation.mutate(child);
        child = population.improvement.improve(child);

        int childWeight = Utils.calculateWeightOfSet(population, child);
        int indexOfSetLessWeight = Utils.findSetWithMinWeight(population);

        if(canInsert(childWeight, indexOfSetLessWeight)){
            setPopulation(child, indexOfSetLessWeight);
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

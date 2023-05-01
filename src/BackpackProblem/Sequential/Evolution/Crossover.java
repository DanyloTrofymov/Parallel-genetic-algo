package BackpackProblem.Sequential.Evolution;

import BackpackProblem.Sequential.Item;
import BackpackProblem.Sequential.Population;

import java.util.ArrayList;
import java.util.List;

public class Crossover {
    Population population;
    public Crossover(Population population) {
        this.population = population;
    }

    public int crossover(Boolean[] parent1, Boolean[] parent2, boolean direct){

        Boolean[] child1 = getChild(parent1, parent2);

        int child1Weight = Utils.calculateWeightOfSet(population, child1);
        int indexOfSetLessWeight = Utils.findSetWithMinWeight(population);

        if(canInsert(child1Weight, indexOfSetLessWeight)){
            setPopulation(child1, indexOfSetLessWeight);
            return indexOfSetLessWeight;
        }

        return -1;
    }

    private Boolean[] getChild(Boolean[] parent1, Boolean[] parent2){
        Boolean[] child = new Boolean[Item.COUNT_OF_ITEMS];
        for (int i = 0; i < Item.COUNT_OF_ITEMS; i++) {
            if (i < Population.CROSSING_FACTOR) {
                child[i] = parent1[i];
            } else {
                child[i] = parent2[i];
            }
        }
        return child;
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
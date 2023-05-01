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

    public Boolean[] getChild(Boolean[] parent1, Boolean[] parent2){
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
}
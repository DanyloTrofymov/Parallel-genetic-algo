package BackpackProblem.Parallel.Evolution;

import BackpackProblem.Parallel.Item;
import BackpackProblem.Parallel.Population;

import java.util.Random;

public class Mutation {
    Population population;
    public Mutation(Population population) {
        this.population = population;
    }

    public Boolean[] mutate(Boolean[] child){
        int mutation = (int) (Math.random() * 101);
        int itemToChange = (int) (Math.random() * Item.COUNT_OF_ITEMS);
        if(mutation <= Population.MUTATION_FACTOR){
            child[itemToChange] = !child[itemToChange];
            if(Utils.calculateWeightOfSet(population, child) > Population.CAPACITY){
                child[itemToChange] = !child[itemToChange];
            }
        }
        return child;
    }
}

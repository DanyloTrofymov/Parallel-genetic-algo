package BackpackProblem.Sequential.Evolution;

import BackpackProblem.Sequential.Item;
import BackpackProblem.Sequential.Population;

import java.util.Random;

public class Mutation {
    Population population;
    public Mutation(Population population) {
        this.population = population;
    }

    public void mutate(int childIndex){
        int mutation = (int) (Math.random() * 101);
        int itemToChange = (int) (Math.random() * Item.COUNT_OF_ITEMS);
        if(mutation <= Population.MUTATION_FACTOR){
            Boolean[] temp = new Boolean[Item.COUNT_OF_ITEMS];
            System.arraycopy(population.currentPopulation[childIndex], 0, temp, 0, Item.COUNT_OF_ITEMS);
            temp[itemToChange] = !temp[itemToChange];
            if(calculateWeightOfSet(temp) <= Population.CAPACITY){
                population.currentPopulation[childIndex][itemToChange] = temp[itemToChange];
            }
        }
    }

    private int calculateWeightOfSet(Boolean[] set){
        int weight = 0;
        for (int i = 0; i < Item.COUNT_OF_ITEMS; i++) {
            if (set[i]) {
                weight += population.items.get(i).weight;
            }
        }
        return weight;
    }
}

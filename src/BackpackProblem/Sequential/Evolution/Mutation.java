package BackpackProblem.Sequential.Evolution;

import BackpackProblem.Sequential.Item;
import BackpackProblem.Sequential.Population;

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
            if(calculateWeightOfSet(child) > Population.CAPACITY){
                child[itemToChange] = !child[itemToChange];
            }
        }
        return child;
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

    public static Boolean[][] shuffle(Boolean[][] allSets){
        Boolean[][] shuffledSets = new Boolean[allSets.length][Item.COUNT_OF_ITEMS];
        Random random = new Random();
        for(int i = 0; i < allSets.length; i++){
            int randomIndex = random.nextInt(allSets.length);
            Boolean[] temp = shuffledSets[i];
            shuffledSets[i] = shuffledSets[randomIndex];
            shuffledSets[randomIndex] = temp;
        }
        return shuffledSets;
    }
}

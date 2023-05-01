package BackpackProblem.Sequential.Evolution;

import BackpackProblem.Sequential.Item;
import BackpackProblem.Sequential.Population;

public class Utils {
    public static int calculateCostOfSet(Population population, Boolean[] set){
        int cost = 0;
        for (int i = 0; i < Item.COUNT_OF_ITEMS; i++) {
            if (set[i]) {
                cost += population.items.get(i).cost;
            }
        }
        return cost;
    }

    public static int calculateWeightOfSet(Population population, Boolean[] set){
        int weight = 0;
        for (int i = 0; i < Item.COUNT_OF_ITEMS; i++) {
            if (set[i]) {
                weight += population.items.get(i).weight;
            }
        }
        return weight;
    }

    public static int findSetWithMinWeight(Population population) {
        int minWeight = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < population.currentPopulation.length; i++) {
            int currentWeight = calculateWeightOfSet(population, population.currentPopulation[i]);
            if (currentWeight < minWeight) {
                minWeight = currentWeight;
                index = i;
            }
        }
        return index;
    }

    public static int findSetWithMaxCost(Population population){
        int maxCost = 0;
        int index = -1;
        for (int i = 0; i < population.currentPopulation.length; i++) {
            int currentCost = calculateCostOfSet(population, population.currentPopulation[i]);
            if (currentCost > maxCost) {
                maxCost = currentCost;
                index = i;
            }
        }
        return index;
    }

    public static void shuffle(Boolean[][] population){
        for(int i = 0; i < population.length; i++){
            int randomIndex = (int) (Math.random() * population.length);
            Boolean[] temp = population[i];
            population[i] = population[randomIndex];
            population[randomIndex] = temp;
        }
    }

}

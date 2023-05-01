package BackpackProblem.Sequential.Evolution;

import BackpackProblem.Sequential.Item;
import BackpackProblem.Sequential.Population;

public class LocalImprovement {
    Population population;
    public LocalImprovement(Population population) {
        this.population = population;
    }

    public Boolean[] improve(Boolean[] child) {
        int minWeight = calculateMinWeight(child);
        int maxCosMinWeightIndex = findMaxCostMinWeight(child, minWeight);
        if (maxCosMinWeightIndex != -1) {
            child[maxCosMinWeightIndex] = true;
            int weight = calculateWeightOfSet(child);
            if (weight > Population.CAPACITY) {
                child[maxCosMinWeightIndex] = false;
            }
        }
        return child;
    }


    public int calculateMinWeight(Boolean[] child){
        int minWeight = Integer.MAX_VALUE;
        for(int i = 0; i < Item.COUNT_OF_ITEMS; i++){
            if(!child[i] && population.items.get(i).weight < minWeight){
                minWeight = population.items.get(i).weight;
            }
        }
        return minWeight;
    }
    public int findMaxCostMinWeight(Boolean[] child, int minWeight){
        int maxCost = 0;
        int index = -1;
        for(int i = 0; i < Item.COUNT_OF_ITEMS; i++){
            if(!child[i] && population.items.get(i).weight == minWeight && population.items.get(i).cost > maxCost){
                maxCost = population.items.get(i).cost;
                index = i;
            }
        }
        return index;
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

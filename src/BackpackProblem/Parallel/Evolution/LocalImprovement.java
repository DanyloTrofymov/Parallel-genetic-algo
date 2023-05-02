package BackpackProblem.Parallel.Evolution;

import BackpackProblem.Parallel.Item;
import BackpackProblem.Parallel.Population;

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
            int weight = Utils.calculateWeightOfSet(population, child);
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
}

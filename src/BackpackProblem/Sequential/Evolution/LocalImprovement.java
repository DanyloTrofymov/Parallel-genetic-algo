package BackpackProblem.Sequential.Evolution;

import BackpackProblem.Sequential.Item;
import BackpackProblem.Sequential.Population;

public class LocalImprovement {
    Population population;
    public LocalImprovement(Population population) {
        this.population = population;
    }

    public void improve(int childIndex){
        int minWeight = calculateMinWeight(childIndex);
        int maxCosMinWeightIndex = findMaxCostMinWeight(childIndex, minWeight);
        if(maxCosMinWeightIndex != -1){
            Boolean[] temp = new Boolean[Item.COUNT_OF_ITEMS];
            System.arraycopy(population.currentPopulation[childIndex], 0, temp, 0, Item.COUNT_OF_ITEMS);
            temp[maxCosMinWeightIndex] = true;
            if(calculateWeightOfSet(temp) <= Population.CAPACITY){
                population.currentPopulation[childIndex][maxCosMinWeightIndex] = true;
            }
        }
    }

    public int calculateMinWeight(int childIndex){
        int minWeight = Integer.MAX_VALUE;
        for(int i = 0; i < Item.COUNT_OF_ITEMS; i++){
            if(!population.currentPopulation[childIndex][i] && population.items.get(i).weight < minWeight){
                minWeight = population.items.get(i).weight;
            }
        }
        return minWeight;
    }
    public int findMaxCostMinWeight(int childIndex, int minWeight){
        int maxCost = 0;
        int index = -1;
        for(int i = 0; i < Item.COUNT_OF_ITEMS; i++){
            if(!population.currentPopulation[childIndex][i] && population.items.get(i).weight == minWeight && population.items.get(i).cost > maxCost){
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

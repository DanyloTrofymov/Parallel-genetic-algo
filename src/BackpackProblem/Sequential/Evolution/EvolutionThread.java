package BackpackProblem.Sequential.Evolution;

import BackpackProblem.Sequential.Population;

import java.util.ArrayList;
import java.util.List;

public class EvolutionThread extends Thread {
    Population population;
    Crossover cross;
    Mutation mutation;
    LocalImprovement improvement;
    Boolean[] parent1;
    Boolean[] parent2;
    boolean direct;

    public EvolutionThread (Population population, Boolean[] parent1, Boolean[] parent2, boolean direct, Crossover cross, Mutation mutation, LocalImprovement improvement) {
        this.population = population;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.direct = direct;
        this.cross = cross;
        this.mutation = mutation;
        this.improvement = improvement;

    }
    @Override
    public void run() {
        int index = cross.crossover(parent1, parent2, direct);
        if (index != -1) {
            mutation.mutate(index);
            improvement.improve(index);
        }
    }
}

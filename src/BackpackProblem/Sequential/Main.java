package BackpackProblem.Sequential;

public class Main {
    public static void main(String[] args) {

        var items = Item.readFromFile("items.txt");;
        Population population = new Population(items);
        population.start();
        System.out.println(population.currentPopulation);
    }
}
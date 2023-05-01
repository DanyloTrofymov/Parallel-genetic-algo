package BackpackProblem.Sequential;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 20; i++){
            var items = Item.readFromFile("items.txt");

            Population population = new Population(items);

            long startTime = System.currentTimeMillis();
            population.start();
            long endTime = System.currentTimeMillis();
            System.out.println((endTime - startTime));
        }
    }
}
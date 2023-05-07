 package BackpackProblem.Sequential;

 import BackpackProblem.Sequential.Evolution.Utils;

 import java.util.Arrays;
 import java.util.List;

 public class Main {
    public static void main(String[] args) {
        int countOfItems = 50;
        int countOfPopulations= 50;
        int backpackCapacity = 25;

        List<Integer> coefficient  = List.of(1, 3, 5, 10); //List.of(1, 2, 3, 5, 7, 10, 15, 20);
        coefficient.forEach(k -> {
            startTest(countOfItems*k, countOfPopulations*k, backpackCapacity*k);
        });
    }
    private static void startTest(int countOfItems, int countOfPopulations, int backpackCapacity){
        Item.COUNT_OF_ITEMS=countOfItems;
        Population.COUNT_OF_POPULATIONS=countOfPopulations;
        Population.CAPACITY=backpackCapacity;

        var items = Item.generateItems();
        System.out.println("Count of population: " + Population.COUNT_OF_POPULATIONS +
                "\nCount of items:" + Item.COUNT_OF_ITEMS + "\nBackpack capacity: " + Population.CAPACITY);
        for (int i = 0; i < 1; i++){


            Population population = new Population(items);

            long startTime = System.currentTimeMillis();
            population.start();
            long endTime = System.currentTimeMillis();
            System.out.println((endTime - startTime));
            //population.writeToFile("result.txt");
        }
    }
}
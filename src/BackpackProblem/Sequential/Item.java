package BackpackProblem.Sequential;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.PrintWriter;
public class Item {
    public static final int COUNT_OF_ITEMS = 100;
    public static final int MAX_WEIGHT = 10;
    public static final int MAX_COST = 20;

    public int weight;
    public int cost;

    public Item (int weight, int cost) {
        this.weight = weight;
        this.cost = cost;
    }

    public Item() {
        this.weight = (int) (Math.random() * MAX_WEIGHT) + 1;
        this.cost = (int) (Math.random() * MAX_COST) + 1;
    }

    public static List<Item> generateItems() {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < COUNT_OF_ITEMS; i++) {
            items.add(new Item());
        }
        return items;
    }
    public static List<Item> readFromFile(String fileName) {
        List<Item> items = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split("[=,}]");
                int weight = Integer.parseInt(tokens[1]);
                int cost = Integer.parseInt(tokens[3]);
                items.add(new Item(weight, cost));
            }
        } catch (FileNotFoundException e) {
            System.err.format("File not found: %s%n", fileName);
        }
        return items;
    }
    public static void writeToFile(List<Item> items, String fileName) {
        try {
            PrintWriter printWriter = new PrintWriter(new File(fileName));
            for (Item item : items) {
                printWriter.println(item);
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Item{" +
                "weight=" + weight +
                ", cost=" + cost +
                '}';
    }
}

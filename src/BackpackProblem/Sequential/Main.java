package BackpackProblem.Sequential;

public class Main {
    public static void main(String[] args) {

        var items = Item.generateItems();
        Item.writeToFile(items, "items.txt");
        System.out.println("Generated: \n" + items);
        var itemsFromFile = Item.readFromFile("items.txt");
        System.out.println("Read: \n" + itemsFromFile);
    }
}
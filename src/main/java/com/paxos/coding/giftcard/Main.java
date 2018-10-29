package com.paxos.coding.giftcard;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Usage: java -jar </path/to/executable> </path/to/items/file> <amount to spend> <number of gifts to buy>");
            System.exit(1);
        }

        String pathToFile = args[0];
        Path filePath = null;
        int amount = 0;
        int numToSelect = 0;
        try {
            filePath = Paths.get(pathToFile);
            amount = Integer.parseInt(args[1]);
            numToSelect = Integer.parseInt(args[2]);
        } catch (IllegalArgumentException e) {
            System.out.printf("Exception encountered reading command-line arguments. Exception: %s\n", e.getMessage());
            System.exit(1);
        }
        if (Files.notExists(filePath) || !Files.isReadable(filePath)) {
            System.out.printf("File at path %s does not exist.\n", pathToFile);
            System.exit(1);
        }

        ItemFileReader.FileResult fileResult = ItemFileReader.readFile(filePath, ",");
        Exception exception = fileResult.getException();
        if (exception != null) {
            System.out.printf("Exception encountered reading from file at %s. Exception: %s\n", pathToFile, exception.getMessage());
            System.exit(1);
        }

        List<Item> items = fileResult.getItems();
        if (items == null || items.isEmpty() || items.size() == 1) {
            System.out.println("Items list contains insufficient items.");
            System.exit(1);
        }

        ItemSelector selector = new ItemSelector();
        int cutoff = selector.searchCutoffIndex(items, amount);

        List<Item> selectedItems = selector.selectItemsForAmount(items, cutoff, amount, numToSelect);
        if (selectedItems.size() < numToSelect) {
            System.out.printf("Not possible to buy [%d] items with amount [%d].\n", numToSelect, amount);
        } else {
            selectedItems.forEach(item -> System.out.println(item.toString()));
            int amountSpent = selectedItems.stream().mapToInt(Item::getPriceInCents).sum();
            System.out.println();
            System.out.printf("Amount spent: [%d], amount left: [%d].\n", amountSpent, amount - amountSpent);
        }
    }
}

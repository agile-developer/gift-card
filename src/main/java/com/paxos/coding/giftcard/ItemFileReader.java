package com.paxos.coding.giftcard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class ItemFileReader {

    private ItemFileReader() { /* Private constructor */ }

    static FileResult readFile(Path filePath, String separator) {

        List<Item> items = Collections.emptyList();
        Exception exception = null;
        try {
            items = Files.lines(filePath).map(lineItem -> {
                String[] itemNameAndPrice = lineItem.split(separator);
                return new Item(itemNameAndPrice[0].trim(), Integer.parseInt(itemNameAndPrice[1].trim()));
            }).collect(Collectors.toList());
        } catch (IllegalArgumentException | IOException e) {
            exception = e;
        }

        return new FileResult(items, exception);
    }

    static class FileResult {

        private final List<Item> items;
        private final Exception exception;

        FileResult(List<Item> items, Exception exception) {
            this.items = items;
            this.exception = exception;
        }

        List<Item> getItems() {
            return items;
        }

        Exception getException() {
            return exception;
        }
    }
}

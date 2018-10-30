package com.paxos.coding.giftcard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Convenience class to parse an input file.
 *
 */
class ItemFileReader {

    private ItemFileReader() { /* Private constructor */ }

    /**
     * Parse a file at the given {@code filePath} into a list of {@link Item} objects. If parsing fails for any reason,
     * then the exception information is captured and the result is wrapped up in a {@link FileResult} object.
     *
     * @param filePath complete path to the input file.
     * @param separator character used to separate columns in the file.
     * @return result of parsing, encapsulating either success or failure.
     */
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

    /**
     * Wrapper class containing result from parsing an input file. If parsing is successful, the 'items' will be populated.
     * If parsing fails, the 'exception' will be set to the cause. A caller can then check both attributes to decide its
     * course of action.
     *
     */
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

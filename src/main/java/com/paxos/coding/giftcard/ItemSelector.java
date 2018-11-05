package com.paxos.coding.giftcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Implements the selection logic to choose a specified number of items from a list of items <b>sorted</b> by 'price'.
 *
 */
class ItemSelector {

    /**
     * Select a specific number of items, from a given <b>sorted</b> list, such that the value of the chosen items is
     * minimally under or equal to the amount provided. This method attempts to optimise selection for the number of
     * items to choose, i.e. try to select {@code numToSelect} over spending {@code amount}.
     *
     * @param candidates list of items <b>sorted</b> by price.
     * @param maxAffordableItemIndex the highest index in the list of an affordable item.
     *                               Given by calling {@link #searchCutoffIndex(List, int)}. Items beyond this index should be ignored.
     * @param amount amount (on gift-card) to spend. Value of selected items should be as close or equal to this.
     * @param numToSelect number of items (gifts) to select.
     * @return list of selected items of size lte {@code numToSelect}.
     */
    List<Item> selectItemsForAmount(List<Item> candidates, int maxAffordableItemIndex, int amount, int numToSelect) {

        if (candidates == null || candidates.isEmpty() || candidates.size() == 1 || candidates.size() < numToSelect)
            return Collections.emptyList();

        int numSelected = 0;
        int currentCandidateIndex = maxAffordableItemIndex;
        int amountRemaining = amount;
        int[] successiveCheapestItemTotals = getSuccessiveCheapestItemTotals(candidates, numToSelect);
        List<Item> selectedItems = new ArrayList<>(numToSelect);

        while ((amountRemaining > 0) && (numSelected < numToSelect) && (currentCandidateIndex >= 0)) {
            Item currentCandidate = candidates.get(currentCandidateIndex);
            int currentCandidatePrice = currentCandidate.getPriceInCents();
            if (isOkToSelect(numSelected, numToSelect, amountRemaining,
                currentCandidateIndex, currentCandidatePrice, successiveCheapestItemTotals)) {
                selectedItems.add(currentCandidate);
                amountRemaining -= currentCandidatePrice;
                numSelected++;
            }
            currentCandidateIndex--;
        }

        return selectedItems;
    }

    /**
     * Convenience method to calculate the successive totals of the cheapest {@code numToSelect} items. This will be used
     * as a lookup array when deciding to pick an item {@link #isOkToSelect(int, int, int, int, int, int[])},
     * to ensure that an item's selection leaves room for at least the remaining number of cheapest items.
     * This helps us optimize for number of items to select.
     *
     * @param candidates list of candidates.
     * @param numToSelect number of items to select.
     * @return array containing the successive totals of {@code numToSelect} items.
     */
    private int[] getSuccessiveCheapestItemTotals(List<Item> candidates, int numToSelect) {
        return IntStream.range(0, numToSelect)
                .map(i -> (i == 0)
                    ? candidates.get(i).getPriceInCents()
                    : candidates.get(i - 1).getPriceInCents() + candidates.get(i).getPriceInCents())
                .toArray();
    }

    /**
     * Binary search for the higest index in the <b>sorted</b> {@code items} list, of the item with a price less than {@code amount}.
     * Any items beyond this index can be safely ignored from the actual selection. If {@code amount} is less than or equal to the cheapest item,
     * or if it's greater than the most expensive item, no search is performed and the first or last index is returned accordingly.
     *
     * @param items list of items <b>sorted</b> by price.
     * @param amount amount (on gift-card) to spend.
     * @return index in the list of the item with a price lt {@code amount}. A value of 0 or -1 means the list cannot be processed.
     */
    int searchCutoffIndex(List<Item> items, int amount) {

        if (items == null || items.isEmpty() || items.size() == 1) return -1;

        int start = 0;
        if (items.get(start).getPriceInCents() >= amount) return start;

        int end = items.size() - 1;
        if (items.get(end).getPriceInCents() < amount) return end;

        int cutoff = -1;
        while (start < end) {
            int midpoint = (start + end) / 2;
            Item itemAtMidpoint = items.get(midpoint);
            if (itemAtMidpoint.getPriceInCents() < amount) {
                if (midpoint < end) {
                    Item itemAtRightOfMidpoint = items.get(midpoint + 1);
                    if (itemAtRightOfMidpoint.getPriceInCents() >= amount) {
                        cutoff = midpoint;
                        break;
                    }
                }
                start = midpoint + 1;
            } else {
                if (midpoint > start) {
                    Item itemAtLeftOfMidpoint = items.get(midpoint - 1);
                    if (itemAtLeftOfMidpoint.getPriceInCents() < amount) {
                        cutoff = midpoint - 1;
                        break;
                    }
                }
                end = midpoint - 1;
            }
        }

        return cutoff;
    }

    /**
     * Determines if the item under consideration is ok to select given existing situation so far.
     *
     * @param numSelected number of items already selected.
     * @param numToSelect total number of items to select.
     * @param amountRemaining amount remaining to be spent.
     * @param currentCandidateIndex index of the current index in the items list.
     * @param currentCandidatePrice price of the item being currently considered.
     * @param successiveCheapestItemTotals lookup array containing the successive totals of the cheapest items.
     * @return true if selecting the current candidate improves our chances of meeting our targets, false otherwise.
     */
    private boolean isOkToSelect(int numSelected, int numToSelect, int amountRemaining,
                                 int currentCandidateIndex, int currentCandidatePrice, int[] successiveCheapestItemTotals) {

        if ((numSelected + 1) < numToSelect && currentCandidateIndex > 0) {
            int numRemainingIfCurrentSelected = numToSelect - (numSelected + 1);
            int leastExpensiveItemsPrice = successiveCheapestItemTotals[numRemainingIfCurrentSelected - 1];
            int remainingMinusCandidate = amountRemaining - currentCandidatePrice;

            return remainingMinusCandidate >= leastExpensiveItemsPrice;
        } else {
            return amountRemaining >= currentCandidatePrice;
        }
    }
}

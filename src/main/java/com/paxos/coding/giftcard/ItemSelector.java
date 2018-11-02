package com.paxos.coding.giftcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        if (candidates == null || candidates.isEmpty() || candidates.size() == 1) return Collections.emptyList();

        int numSelected = 0;
        int currentCandidateIndex = maxAffordableItemIndex;
        int amountRemaining = amount;
        List<Item> selectedItems = new ArrayList<>(numToSelect);

        while (amountRemaining > 0 && numSelected < numToSelect && currentCandidateIndex >= 0) {
            Item currentCandidate = candidates.get(currentCandidateIndex);
            int currentCandidatePrice = currentCandidate.getPriceInCents();
            if (isOkToSelect(numSelected, numToSelect, candidates, amountRemaining, currentCandidateIndex, currentCandidatePrice)) {
                selectedItems.add(currentCandidate);
                amountRemaining -= currentCandidatePrice;
                numSelected++;
            }
            currentCandidateIndex--;
        }

        return selectedItems;
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
     * @param items list of items <b>sorted</b> by price.
     * @param amountRemaining amount remaining to be spent.
     * @param currentCandidatePrice price of the item being currently considered.
     * @return true if selecting the current candidate improves our chances of meeting our targets, false otherwise.
     */
    private boolean isOkToSelect(int numSelected, int numToSelect, List<Item> items, int amountRemaining,
                                 int currentCandidateIndex, int currentCandidatePrice) {

        if ((numSelected + 1) < numToSelect && currentCandidateIndex > 0) {
            int numRemainingIfCurrentSelected = numToSelect - (numSelected + 1);
            int leastExpensiveItemsPrice = items.stream().limit(numRemainingIfCurrentSelected).mapToInt(Item::getPriceInCents).sum();
            int remainingMinusCandidate = amountRemaining - currentCandidatePrice;

            return remainingMinusCandidate >= leastExpensiveItemsPrice;
        } else {
            return amountRemaining >= currentCandidatePrice;
        }
    }
}

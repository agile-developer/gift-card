package com.paxos.coding.giftcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ItemSelector {


    List<Item> selectItemsForAmount(List<Item> candidates, int maxAffordableItemIndex, int amount, int numToSelect) {

        if (candidates == null || candidates.isEmpty() || candidates.size() == 1) return Collections.emptyList();

        int numSelected = 0;
        int currentCandidateIndex = maxAffordableItemIndex;
        int amountRemaining = amount;
        List<Item> selectedItems = new ArrayList<>(numToSelect);

        while (amountRemaining > 0 && numSelected < numToSelect && currentCandidateIndex >= 0) {
            Item currentCandidate = candidates.get(currentCandidateIndex);
            int currentCandidatePrice = currentCandidate.getPriceInCents();
            if (isOkToSelect(numSelected, numToSelect, candidates, amountRemaining, currentCandidatePrice)) {
                selectedItems.add(currentCandidate);
                amountRemaining -= currentCandidatePrice;
                numSelected++;
            }
            currentCandidateIndex--;
        }

        return selectedItems;
    }

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

    private boolean isOkToSelect(int numSelected, int numToSelect, List<Item> items, int amountRemaining, int currentCandidatePrice) {

        int remainingMinusCandidate = amountRemaining - currentCandidatePrice;
        if ((numSelected + 1) < numToSelect) {
            int numRemainingIfCurrentSelected = numToSelect - (numSelected + 1);
            int leastExpensiveItemsPrice = items.stream().limit(numRemainingIfCurrentSelected).mapToInt(Item::getPriceInCents).sum();

            return remainingMinusCandidate >= leastExpensiveItemsPrice;
        } else {
            return amountRemaining >= currentCandidatePrice;
        }
    }
}

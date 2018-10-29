package com.paxos.coding.giftcard;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ItemSelectorTest {

    private ItemSelector itemSelector;
    private List<Item> codingChallengeItems;
    private Item item1;
    private Item item2;
    private Item item3;
    private Item item4;
    private Item item5;
    private Item item6;

    @Before
    public void setup() {

        item1 = new Item("Candy Bar", 500);
        item2 = new Item("Paperback Book", 700);
        item3 = new Item("Detergent", 1_000);
        item4 = new Item("Headphones", 1_400);
        item5 = new Item("Earmuffs", 2_000);
        item6 = new Item("Bluetooth Stereo", 6_000);

        codingChallengeItems = Arrays.asList(item1, item2, item3, item4, item5, item6);
        itemSelector = new ItemSelector();

    }

    @Test
    public void check_items_list_with_less_than_two_items_returns_negative_cutoff() {

        Item item1 = new Item("Candy Bar", 500);
        List<Item> items = Collections.singletonList(item1);
        int amountToSpend = 1_000;
        int cutoff = itemSelector.searchCutoffIndex(items, amountToSpend);

        assertThat(cutoff, is(equalTo(-1)));
    }

    @Test
    public void check_two_items_to_buy_from_list_with_two_items_and_sufficient_amount() {

        Item item1 = new Item("Candy Bar", 500);
        Item item2 = new Item("Paperback Book", 700);
        List<Item> items = Arrays.asList(item1, item2);

        int amountToSpend = 1_200;
        int cutoff = itemSelector.searchCutoffIndex(items, amountToSpend);
        List<Item> selectedItems = itemSelector.selectItemsForAmount(items, cutoff, amountToSpend, 2);

        assertThat(selectedItems.size(), is(equalTo(2)));
    }

    @Test
    public void check_two_items_to_buy_from_list_with_two_items_and_insufficient_amount() {

        Item item1 = new Item("Candy Bar", 500);
        Item item2 = new Item("Paperback Book", 700);
        List<Item> items = Arrays.asList(item1, item2);

        int amountToSpend = 900;
        int cutoff = itemSelector.searchCutoffIndex(items, amountToSpend);
        List<Item> selectedItems = itemSelector.selectItemsForAmount(items, cutoff, amountToSpend, 2);

        assertThat(selectedItems.size(), is(equalTo(0)));
    }

    @Test
    public void check_two_items_to_buy_from_list_with_all_items_having_same_price() {

        Item item1 = new Item("Candy Bar", 500);
        Item item2 = new Item("Paperback Book", 500);
        Item item3 = new Item("Hardback Book", 500);
        Item item4 = new Item("Hammer", 500);
        List<Item> items = Arrays.asList(item1, item2, item3, item4);

        int amountToSpend = 1_000;
        int cutoff = itemSelector.searchCutoffIndex(items, amountToSpend);
        List<Item> selectedItems = itemSelector.selectItemsForAmount(items, cutoff, amountToSpend, 2);

        assertThat(selectedItems.size(), is(equalTo(2)));
        assertThat(selectedItems, hasItems(item4, item3));
    }

    @Test
    public void check_test_cases_from_coding_challenge_description() {

        int amountToSpend = 2_500;
        int cutoff = itemSelector.searchCutoffIndex(codingChallengeItems, amountToSpend);
        List<Item> selectedItems = itemSelector.selectItemsForAmount(codingChallengeItems, cutoff, amountToSpend, 2);

        assertThat(selectedItems.size(), is(equalTo(2)));
        assertThat(selectedItems, hasItems(item5, item1));

        amountToSpend = 2_300;
        cutoff = itemSelector.searchCutoffIndex(codingChallengeItems, amountToSpend);
        selectedItems = itemSelector.selectItemsForAmount(codingChallengeItems, cutoff, amountToSpend, 2);

        assertThat(selectedItems.size(), is(equalTo(2)));
        assertThat(selectedItems, hasItems(item4, item2));

        amountToSpend = 10_000;
        cutoff = itemSelector.searchCutoffIndex(codingChallengeItems, amountToSpend);
        selectedItems = itemSelector.selectItemsForAmount(codingChallengeItems, cutoff, amountToSpend, 2);

        assertThat(selectedItems.size(), is(equalTo(2)));
        assertThat(selectedItems, hasItems(item6, item5));

        amountToSpend = 1_100;
        cutoff = itemSelector.searchCutoffIndex(codingChallengeItems, amountToSpend);
        selectedItems = itemSelector.selectItemsForAmount(codingChallengeItems, cutoff, amountToSpend, 2);

        assertThat(selectedItems.size(), is(equalTo(1)));
        assertThat(selectedItems, hasItem(item1));
    }

    @Test
    public void check_three_items_to_buy_from_coding_challenge_description() {

        int amountToSpend = 2_500;
        int cutoff = itemSelector.searchCutoffIndex(codingChallengeItems, amountToSpend);
        List<Item> selectedItems = itemSelector.selectItemsForAmount(codingChallengeItems, cutoff, amountToSpend, 3);

        assertThat(selectedItems.size(), is(equalTo(3)));
        assertThat(selectedItems, hasItems(item3, item2, item1));

        amountToSpend = 2_300;
        cutoff = itemSelector.searchCutoffIndex(codingChallengeItems, amountToSpend);
        selectedItems = itemSelector.selectItemsForAmount(codingChallengeItems, cutoff, amountToSpend, 3);

        assertThat(selectedItems.size(), is(equalTo(3)));
        assertThat(selectedItems, hasItems(item3, item2, item1));

        amountToSpend = 10_000;
        cutoff = itemSelector.searchCutoffIndex(codingChallengeItems, amountToSpend);
        selectedItems = itemSelector.selectItemsForAmount(codingChallengeItems, cutoff, amountToSpend, 3);

        assertThat(selectedItems.size(), is(equalTo(3)));
        assertThat(selectedItems, hasItems(item6, item5, item4));
    }

    @Test
    public void check_four_items_to_buy_from_coding_challenge_description() {

        int amountToSpend = 5_100;
        int cutoff = itemSelector.searchCutoffIndex(codingChallengeItems, amountToSpend);
        List<Item> selectedItems = itemSelector.selectItemsForAmount(codingChallengeItems, cutoff, amountToSpend, 4);

        assertThat(selectedItems.size(), is(equalTo(4)));
        assertThat(selectedItems, hasItems(item5, item4, item3, item2));
    }
}

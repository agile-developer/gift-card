package com.paxos.coding.giftcard;

import java.util.Objects;

public class Item {

    private final String name;
    private final int priceInCents;

    public Item(String name, int priceInCents) {
        this.name = name;
        this.priceInCents = priceInCents;
    }

    public String getName() {
        return name;
    }

    public int getPriceInCents() {
        return priceInCents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return priceInCents == item.priceInCents &&
                Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, priceInCents);
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", priceInCents=" + priceInCents +
                '}';
    }
}

package org.poo.graph;

public class Node {
    private final String currency;
    private final double rate;

    public Node(final String currency,
                final double rate) {
        this.currency = currency;
        this.rate = rate;
    }

    /**
     * Returns the exchange rate associated with this node.
     *
     * @return The exchange rate as a {@code double}.
     */
    public double getRate() {
        return rate;
    }

    /**
     * Returns the currency associated with this node.
     *
     * @return The currency as a {@code String}.
     */
    public String getCurrency() {
        return currency;
    }

}

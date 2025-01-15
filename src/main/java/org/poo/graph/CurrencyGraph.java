package org.poo.graph;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrencyGraph {
    private HashMap<String, ArrayList<Node>> graph;
    private HashMap<String, Boolean> visited;

    public CurrencyGraph() {
        graph = new HashMap<>();
        visited = new HashMap<>();
    }

    /**
     * Returns the map of currencies and their visited status during DFS.
     *
     * @return A {@link HashMap} where the key is the currency name,
     * and the value is a {@code Boolean} indicating if the currency has been visited.
     */
    public HashMap<String, Boolean> getVisited() {
        return visited;
    }

    /**
     * Returns the graph that represents currency exchange rates.
     *
     * @return A {@link HashMap} where the key is the currency name,
     * and the value is a list of {@link Node} objects
     * representing neighboring currencies and their exchange rates.
     */
    public HashMap<String, ArrayList<Node>> getGraph() {
        return graph;
    }

    /**
     * Sets the graph that represents currency exchange rates.
     *
     * @param graph A {@link HashMap} representing the graph,
     *              where the key is the currency name,
     *              and the value is a list of {@link Node} objects
     *              representing neighboring currencies.
     */
    public void setGraph(final HashMap<String, ArrayList<Node>>
                                 graph) {
        this.graph = graph;
    }

    /**
     * Sets the visited map, which tracks the currencies visited during DFS.
     *
     * @param visited A {@link HashMap} where the key is the currency name,
     *                and the value is a {@code Boolean}
     *                indicating if the currency has been visited.
     */
    public void setVisited(final HashMap<String, Boolean> visited) {
        this.visited = visited;
    }

    /**
     * Adds an edge (exchange rate) between two currencies in the graph.
     * Also adds the reverse rate for bidirectional exchange.
     * Initializes the visited status for both currencies.
     *
     * @param curr1 The first currency.
     * @param curr2 The second currency.
     * @param rate  The exchange rate from {@code curr1} to {@code curr2}.
     */
    public void addEdge(final String curr1,
                        final String curr2,
                        final double rate) {
        try {
            double rateTwoToOne = 1 / rate;
            if (!graph.containsKey(curr1)) {
                graph.put(curr1, new ArrayList<>());
            }
            graph.get(curr1).add(new Node(curr2, rate));
            if (!graph.containsKey(curr2)) {
                graph.put(curr2, new ArrayList<>());
            }
            graph.get(curr2).add(new Node(curr1, rateTwoToOne));
            visited.put(curr1, false);
            visited.put(curr2, false);
        } catch (ArithmeticException e) {
            System.out.println("Cannot divide by 0");
        }
    }

    /**
     * Performs a DFS to find the exchange rate between two currencies.
     * If no valid exchange path exists, returns -1.
     *
     * @param start The starting currency node.
     * @param end   The target currency node.
     * @param amount The amount to be converted.
     * @return The converted amount if a path exists, otherwise -1.
     * @throws IllegalArgumentException if the start currency
     *  does not exist in the graph.
     */
    public double exchangeDFS(final Node start,
                              final Node end,
                              final double amount) throws IllegalArgumentException {
        if (!graph.containsKey(start.getCurrency())) {
            throw new IllegalArgumentException("The starting currency doesn't exist");
        }
        visited.put(start.getCurrency(), true);
        int idOfInterestCurrency = -1;
        for (int i = 0; i < graph.get(start.getCurrency()).size(); i++) {
            if (graph.get(start.getCurrency()).get(i).getCurrency().equals(end.getCurrency())) {
                idOfInterestCurrency = i;
            }
        }
        if (idOfInterestCurrency != -1) {
            return amount * graph
                            .get(start.getCurrency())
                            .get(idOfInterestCurrency)
                            .getRate();
        }
        for (Node adjNode: graph.get(start.getCurrency())) {
            if (!visited.get(adjNode.getCurrency())) {
                double ret = exchangeDFS(adjNode, end, amount * adjNode.getRate());
                if (ret != -1) {
                    return ret;
                }
            }
        }
        return -1;
    }

    /**
     * Resets the visited status for all currencies in the graph to {@code false}.
     */
    void resetVisited() {
        for (String currency: visited.keySet()) {
            visited.put(currency, false);
        }
    }

    /**
     * Initiates a currency exchange operation,
     * using DFS to find a valid exchange path.
     * If no valid path is found,
     * an exception is thrown, and the method returns -1.
     *
     * @param start   The starting currency node.
     * @param end     The target currency node.
     * @param amount  The amount to be converted.
     * @return The converted amount if a valid path is found, otherwise -1.
     */
    public double exchange(final Node start,
                           final Node end,
                           final double amount) {
        if (start.getCurrency().equals(end.getCurrency())) {
            return amount;
        }
        resetVisited();
        try {
            return exchangeDFS(start, end, amount);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }
}

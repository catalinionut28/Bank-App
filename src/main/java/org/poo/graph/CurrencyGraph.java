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

    public HashMap<String, Boolean> getVisited() {
        return visited;
    }

    public HashMap<String, ArrayList<Node>> getGraph() {
        return graph;
    }

    public void setGraph(HashMap<String, ArrayList<Node>> graph) {
        this.graph = graph;
    }

    public void setVisited(HashMap<String, Boolean> visited) {
        this.visited = visited;
    }

    public void addEdge(String curr1, String curr2, double rate) {
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

    public double exchangeDFS(Node start, Node end, double amount) throws IllegalArgumentException {
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
        System.out.println(idOfInterestCurrency);
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

    void resetVisited() {
        for (String currency: visited.keySet()) {
            visited.put(currency, false);
        }
    }

    public double exchange(Node start, Node end, double amount) {
        resetVisited();
        try {
            return exchangeDFS(start, end, amount);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }
}

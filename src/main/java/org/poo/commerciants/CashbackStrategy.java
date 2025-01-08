package org.poo.commerciants;

public interface CashbackStrategy {
    double getCashback(double amount);
    String getStrategy();
}

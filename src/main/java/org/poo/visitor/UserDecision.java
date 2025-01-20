package org.poo.visitor;

public interface UserDecision {
    public void accept(PaymentVisitor payment);
    public void reject(PaymentVisitor payment);
}

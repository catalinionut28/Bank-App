package org.poo.visitor;
import org.poo.bank.User;

public interface PaymentVisitor {
    public void visit(User user);
}

package org.poo.visitor;
import org.poo.bank.User;

public interface PaymentVisitor {

    /**
     * Visits and processes a payment for the given user.
     * <p>
     * This method is implemented to perform
     * specific actions related to the user,
     * such as accepting, rejecting, or handling payments for that user.
     * </p>
     *
     * @param user the user whose payment is being processed
     */
    void visit(User user);
}

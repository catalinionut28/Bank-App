package org.poo.visitor;

public interface UserDecision {

    /**
     * Accepts the payment.
     * <p>
     * This method allows the user to accept a payment,
     * typically triggering the
     * necessary actions to complete the payment process.
     * </p>
     *
     * @param payment the payment visitor handling the payment process
     */
    void accept(PaymentVisitor payment);

    /**
     * Rejects the payment.
     * <p>
     * This method allows the user to reject a payment,
     * preventing the payment from
     * being processed further.
     * </p>
     *
     * @param payment the payment visitor handling the payment process
     */
    void reject(PaymentVisitor payment);
}

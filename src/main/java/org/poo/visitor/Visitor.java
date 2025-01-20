package org.poo.visitor;

import org.poo.bank.PendingPayment;
import org.poo.bank.SplitPaymentTransaction;
import org.poo.bank.Transaction;
import org.poo.bank.User;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Visitor implements PaymentVisitor {
    private boolean accept;

    public Visitor(boolean accept) {
        this.accept = accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    @Override
    public void visit(User user) {
        try {
            ArrayList<String> ibanInvolved = new ArrayList<>();
            PendingPayment pendingPayment = user.getPendingPayments().getFirst();
            System.out.println(pendingPayment.getAmount() + " Suma de plata");
            System.out.println(pendingPayment.getAccountInvolved().getBalance() + " Suma din balanta");
            if (accept) {
                pendingPayment.accept();
                user.getPendingPayments().remove(pendingPayment);
                for (PendingPayment payment : pendingPayment.getSplitPayment().getPendingPayments()) {
                    if (!payment.isAccepted()) {
                        return;
                    }
                }
                for (PendingPayment payment : pendingPayment.getSplitPayment().getPendingPayments()) {
                    // create a new transaction and sort
                    ibanInvolved.add(payment.getAccountInvolved().getIban());
                    System.out.println(payment.getAccountInvolved().getIban());
                }
                for (PendingPayment payment : pendingPayment.getSplitPayment().getPendingPayments()) {
                    if (payment.getAccountInvolved().getBalance() < payment.getAmount()) {
                        String description = "Split payment of "
                                + String.format("%.2f", pendingPayment.getSplitPayment().getAmount())
                                + " "
                                + pendingPayment.getSplitPayment().getCurrency();
                        SplitPaymentTransaction splitPaymentTransaction = new SplitPaymentTransaction(pendingPayment.getTimestamp(),
                                description, pendingPayment.getSplitPayment().getType(), pendingPayment.getSplitPayment().getCurrency(),
                                pendingPayment.getSplitPayment().getAmount(), ibanInvolved, pendingPayment.getSplitPayment().getOriginalAmounts(),
                                "Account " + payment.getAccountInvolved().getIban() + " has insufficient funds for a split payment.");
                        pendingPayment.getSplitPayment()
                                .getPendingPayments()
                                .forEach(p -> p.getAccountInvolved().getUserTransactions().add(splitPaymentTransaction));
                        pendingPayment.getSplitPayment()
                                .getPendingPayments()
                                .forEach(p -> p.getAccountInvolved()
                                        .getUserTransactions()
                                        .sort(Transaction::compareTo));
                        for (int i = 0; i < pendingPayment.getSplitPayment().getUsers().size(); i++) {
                            PendingPayment p = pendingPayment.getSplitPayment().getPendingPayments().get(i);
                            User usr = pendingPayment.getSplitPayment().getUsers().get(i);
                            if (!pendingPayment.isAccepted()) {
                                usr.getPendingPayments().remove(p);
                            }
                        }
                        return;
                    }
                }
                String description = "Split payment of "
                        + String.format("%.2f", pendingPayment.getSplitPayment().getAmount())
                        + " "
                        + pendingPayment.getSplitPayment().getCurrency();
                System.out.println(ibanInvolved);
                SplitPaymentTransaction splitPaymentTransaction = new SplitPaymentTransaction(pendingPayment.getTimestamp(),
                        description, pendingPayment.getSplitPayment().getType(), pendingPayment.getSplitPayment().getCurrency(),
                        pendingPayment.getSplitPayment().getAmount(), ibanInvolved, pendingPayment.getSplitPayment().getOriginalAmounts(),
                        null);
                pendingPayment.getSplitPayment()
                        .getPendingPayments()
                        .forEach(p -> p.getAccountInvolved().getUserTransactions().add(splitPaymentTransaction));
                pendingPayment.getSplitPayment().getPendingPayments()
                        .forEach(p -> p.getAccountInvolved()
                                .getUserTransactions()
                                .sort(Transaction::compareTo));
                pendingPayment.getSplitPayment().getPendingPayments()
                        .forEach(p -> p.getAccountInvolved().splitPay(p.getAmount()));
            } else {
                pendingPayment.reject();
                user.getPendingPayments().remove(pendingPayment);
                for (PendingPayment payment : pendingPayment.getSplitPayment().getPendingPayments()) {
                    // create a new failed transaction and sort it
                    ibanInvolved.add(payment.getAccountInvolved().getIban());
                }
                String description = "Split payment of "
                        + String.format("%.2f", pendingPayment.getSplitPayment().getAmount())
                        + " "
                        + pendingPayment.getSplitPayment().getCurrency();

                SplitPaymentTransaction splitPaymentTransaction = new SplitPaymentTransaction(pendingPayment.getTimestamp(),
                        description, pendingPayment.getSplitPayment().getType(), pendingPayment.getSplitPayment().getCurrency(),
                        pendingPayment.getSplitPayment().getAmount(), ibanInvolved, pendingPayment.getSplitPayment().getOriginalAmounts(),
                        "One user rejected the payment.");
                pendingPayment.getSplitPayment()
                        .getPendingPayments()
                        .forEach(p -> p.getAccountInvolved().getUserTransactions().add(splitPaymentTransaction));
                pendingPayment.getSplitPayment().getPendingPayments()
                        .forEach(p -> p.getAccountInvolved()
                                .getUserTransactions()
                                .sort(Transaction::compareTo));
                for (int i = 0; i < pendingPayment.getSplitPayment().getUsers().size(); i++) {
                    PendingPayment payment = pendingPayment.getSplitPayment().getPendingPayments().get(i);
                    User usr = pendingPayment.getSplitPayment().getUsers().get(i);
                    if (!pendingPayment.isAccepted()) {
                        usr.getPendingPayments().remove(payment);
                    }
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("The account doesn't have pending transactions left");
        }
    }
}

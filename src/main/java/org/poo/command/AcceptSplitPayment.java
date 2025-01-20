package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.User;
import org.poo.visitor.Visitor;

class AcceptSplitPayment implements Command {
    private User user;
    private int timestamp;
    private ArrayNode output;
    private ObjectMapper objectMapper;

    AcceptSplitPayment(final User user,
                       final int timestamp,
                       final ArrayNode output,
                       final ObjectMapper objectMapper) {
        this.user = user;
        this.timestamp = timestamp;
        this.output = output;
        this.objectMapper = objectMapper;
    }


    /**
     * Executes the command to accept a split payment for the current user.
     * <p>
     * If the user is not found (i.e., user is null),
     * an error message is returned with the command type
     * "acceptSplitPayment" and a description of "User not found."
     * If the user exists, a {@link Visitor} object
     * is created with a true flag (indicating
     * acceptance) and passed to the user's accept method.
     * </p>
     *
     * @see Visitor
     * @see User #accept(UserDecision)
     */
    @Override
    public void execute() {
        if (user == null) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "acceptSplitPayment");
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("timestamp", timestamp);
            errorNode.put("description", "User not found");
            outputNode.set("output", errorNode);
            outputNode.put("timestamp", timestamp);
            output.add(outputNode);
            return;
        }
        Visitor visitor = new Visitor(true);
        user.accept(visitor);
    }
}

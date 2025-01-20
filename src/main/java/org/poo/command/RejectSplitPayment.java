package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.User;
import org.poo.visitor.Visitor;

class RejectSplitPayment implements Command {
    private User user;
    private int timestamp;
    private ArrayNode output;
    private ObjectMapper objectMapper;

    RejectSplitPayment(final User user,
                       final int timestamp,
                       final ArrayNode output,
                       final ObjectMapper objectMapper) {
        this.user = user;
        this.timestamp = timestamp;
        this.output = output;
        this.objectMapper = objectMapper;
    }

    /**
     * Executes the command to reject a split payment.
     * <p>
     * If the user is found, the
     * rejection is processed using a {@link Visitor}.
     * If the user is not
     * found, an error message is generated with
     * the description "User not found" and added to the output.
     * </p>
     */
    @Override
    public void execute() {
        if (user == null) {
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("command", "rejectSplitPayment");
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("timestamp", timestamp);
            errorNode.put("description", "User not found");
            outputNode.set("output", errorNode);
            outputNode.put("timestamp", timestamp);
            output.add(outputNode);
            return;
        }
        Visitor visitor = new Visitor(false);
        user.reject(visitor);
    }
}

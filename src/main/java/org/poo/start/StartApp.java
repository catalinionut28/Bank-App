package org.poo.start;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.command.Client;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import org.poo.utils.Utils;


public class StartApp {

    public static void start(ObjectInput objectInput, ObjectMapper objectMapper,
                        ArrayNode output) {
        objectMapper = new ObjectMapper();
        Client client = new Client(objectInput, objectMapper, output);
        for (CommandInput commandInput: objectInput.getCommands()) {
            try {
                client.executeAction(commandInput.getCommand(), commandInput);
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
        Utils.resetRandom();
    }


}

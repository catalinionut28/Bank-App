package org.poo.start;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.command.Client;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.utils.Utils;


public final class StartApp {
    private static StartApp instance;

    private StartApp() {

    }
    /**
     * Returns the singleton instance of the {@code StartApp} class.
     * This method ensures that only one instance of the {@code StartApp} class is created
     * (following the Singleton design pattern). If an instance does not already exist,
     * it creates one; otherwise, it returns the existing instance.
     * @return the singleton instance of {@code StartApp}
     */
    public static StartApp getInstance() {
        if (instance == null) {
            instance = new StartApp();
        }
        return instance;
    }

    private void clear() {
        instance = null;
    }

    /**
     * Starts the execution of commands from the provided {@link ObjectInput}.
     * It creates a {@link Client} instance
     * and processes each command in the list.
     * For each command, the appropriate action is executed.
     * If an {@link IllegalArgumentException} is thrown during the execution of a command,
     * it is caught, and the next command
     * is processed. Once all commands are processed, the random number generator is reset.
     *
     * @param objectInput  The {@link ObjectInput}
     *                     containing the commands to be executed.
     * @param output       The {@link ArrayNode}
     *                     where the results of the executed commands will be stored.
     */
    public void start(final ObjectInput objectInput,
                        final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        Client client = new Client(objectInput, objectMapper, output);
        for (CommandInput commandInput: objectInput.getCommands()) {
            try {
                client.executeAction(commandInput.getCommand(), commandInput);
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
        Utils.resetRandom();
        clear();
    }


}

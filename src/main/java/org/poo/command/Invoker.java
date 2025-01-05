package org.poo.command;

public class Invoker {

    /**
     * Executes the provided command by invoking its {@link Command#execute()} method.
     * The {@code execute()} method triggers
     * the action associated with the command,
     * allowing the command to perform its logic without the {
     * @code Invoker} needing to know the details
     * of the action being performed.
     *
     * @param command The command to be executed.
     */
    public void execute(final Command command) {
        command.execute();
    }
}

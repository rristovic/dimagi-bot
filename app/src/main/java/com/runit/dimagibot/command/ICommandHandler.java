package com.runit.dimagibot.command;

/**
 * Handles all of the work that a {@link Command} represents.
 */
public interface ICommandHandler {
    /**
     * Respondes with certain action according to command.
     *
     * @param command
     */
    void handleCommand(Command command);
}

package com.runit.dimagibot.command;

public interface ICommandProcessor {
    void processCommand(Command command) throws ProcessingException;
    Command parseCommand(String rawCommandString) throws ProcessingException;
}

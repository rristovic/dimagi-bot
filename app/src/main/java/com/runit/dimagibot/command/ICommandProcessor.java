package com.runit.dimagibot.command;

public interface ICommandProcessor {
    void processCommand(String rawCommandString) throws ProcessingException;
}

package com.runit.dimagibot.command;

public class CommandProcessor implements ICommandProcessor {

    private final ICommandHandler mHandler;
    private static final String NUM_REGEX = "[0-9]+";

    public CommandProcessor(ICommandHandler handler) {
        mHandler = handler;
    }

    @Override
    public void processCommand(String rawCommandString) throws ProcessingException {
        // assert
        if (rawCommandString == null || rawCommandString.isEmpty())
            throw new ProcessingException("Can't process empty input.");

        // Reduce user input errors
        rawCommandString = processBadInput(rawCommandString);

        // make all lower in order to process commands
        Command.CommandType type = tryProcessingType(rawCommandString);
        String data = tryProcessingData(rawCommandString, type);
        Command cmd = new Command(type, data);
        mHandler.handleCommand(cmd);
    }

    /**
     * Tries to extract data/arguments for the given command.
     *
     * @param rawCommandString
     * @param type
     * @return
     */
    private String tryProcessingData(String rawCommandString, Command.CommandType type) throws ProcessingException {
        String dataString = rawCommandString.substring(type.rawText.length() + 1);
        dataString = dataString.trim();
        assertInput(dataString, type);
        return dataString;
    }

    private void assertInput(String dataString, Command.CommandType type) throws ProcessingException {
        // sanity assert
        if (dataString.isEmpty())
            throw new ProcessingException("No arguments for the given command.");

        if (type.equals(Command.CommandType.EDIT)) {
            // for edit actions, there should be [index, dataString]
            if(!dataString.contains(" "))
                throw new ProcessingException("Invalid arguments for EDIT command.");

            String numData = dataString.substring(0, dataString.indexOf(" "));
            if (!numData.matches(NUM_REGEX)) {
                throw new ProcessingException("Invalid item index for EDIT command.");
            }
        } else if (type.equals(Command.CommandType.REMOVE) || type.equals(Command.CommandType.MARK_DONE)) {
            if (!dataString.matches(NUM_REGEX)) {
                throw new ProcessingException("Invalid item index for " + type.rawText.toUpperCase() + " command.");
            }
            int indx = Integer.parseInt(dataString);
            if (indx <= 0)
                throw new ProcessingException("Invalid index argument.");
        }
    }

    /**
     * Tries to extract command type which should be at the begging of the string.
     * If no known command is found, it will raise an exception.
     *
     * @return
     * @throws ProcessingException
     */
    private Command.CommandType tryProcessingType(String rawCommandString) throws ProcessingException {
        String command = rawCommandString.substring(0, rawCommandString.indexOf(" "));
        command = command.toLowerCase();
        if (command.equals(Command.CommandType.ADD.rawText)) {
            return Command.CommandType.ADD;
        } else if (command.equals(Command.CommandType.EDIT.rawText)) {
            return Command.CommandType.EDIT;
        } else if (command.equals(Command.CommandType.MARK_DONE.rawText)) {
            return Command.CommandType.MARK_DONE;
        } else if (command.equals(Command.CommandType.REMOVE.rawText)) {
            return Command.CommandType.REMOVE;
        } else if (command.equals(Command.CommandType.REMINDER.rawText)) {
            return Command.CommandType.REMINDER;
        }
        throw new ProcessingException("Invalid command.");
    }

    private String processBadInput(String rawCommandString) throws ProcessingException {
        // reduce user input errors
        rawCommandString = rawCommandString.trim();
        if (!rawCommandString.contains(" ")) {
            throw new ProcessingException("Invalid command input.");
        }
        return rawCommandString;
    }

}

package com.runit.dimagibot.command;

import com.runit.dimagibot.data.ITodoRepository;
import com.runit.dimagibot.data.model.TodoItem;

public class CommandHandler implements ICommandHandler {
    private final ITodoRepository mRepo;

    public CommandHandler(ITodoRepository repository) {
        mRepo = repository;
    }

    @Override
    public void handleCommand(Command cmd) {
        switch (cmd.getType()) {
            case ADD:
                addNewItem(cmd.getData());
                break;
            case EDIT:
                editItem(parseIndexFromCommandData(cmd.getData())
                        , deleteIndexFromCommandData(cmd.getData()));
                break;
            case REMOVE:
                removeItem(cmd.getData());
                break;
            case MARK_DONE:
                markDone(cmd.getData());
                break;

            case REMINDER:
            default:
                break;
        }
    }

    private void markDone(String index) {
        int idx = parseIndex(index);
        mRepo.markDone(idx);
    }

    private void removeItem(String index) {
        int idx = parseIndex(index);
        mRepo.delete(idx);
    }

    /**
     * Edits item with provided index, replacing old body text with new one.
     *
     * @param itemIndex
     * @param itemContent
     */
    private void editItem(int itemIndex, String itemContent) {
        mRepo.update(itemIndex, itemContent);
    }

    /**
     * Adds new {@link TodoItem} into database.
     *
     * @param data
     */
    private void addNewItem(String data) {
        mRepo.insert(data);
    }

    private int parseIndexFromCommandData(String commandData) {
        return parseIndex(commandData.substring(0, commandData.indexOf(" ")));
    }

    private String deleteIndexFromCommandData(String commandData) {
        return commandData.substring(commandData.indexOf(" ") + 1);
    }


    private int parseIndex(String data) {
        // -1 because user will likely see numbers starting from 1,2,3.. and not 0,1,2...
        return Integer.parseInt(data) - 1;
    }
}

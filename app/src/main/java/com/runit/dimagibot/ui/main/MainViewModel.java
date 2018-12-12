package com.runit.dimagibot.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.runit.dimagibot.command.Command;
import com.runit.dimagibot.command.ICommandProcessor;
import com.runit.dimagibot.command.ProcessingException;
import com.runit.dimagibot.data.ITodoRepository;
import com.runit.dimagibot.data.model.TodoItem;
import com.runit.dimagibot.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel {

    private final ICommandProcessor mProcessor;
    private final ITodoRepository mRepo;
    private SingleLiveEvent<String> mMessage = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> mDisplayHelp = new MutableLiveData<>();
    private MediatorLiveData<List<TodoItem>> mItems = new MediatorLiveData<>();
    private LiveData<List<TodoItem>> mSource;
    private boolean mShowCompletedOnly, mShowUncompletedOnly;

    public MainViewModel(ICommandProcessor processor, ITodoRepository repository) {
        mProcessor = processor;
        mRepo = repository;
        mSource = mRepo.getItems();
        refreshData();
    }

    // ================================
    // Public API
    // ================================

    void onNewCommand(String command) {
        try {
            Command cmd = mProcessor.parseCommand(command);

            if ((mShowCompletedOnly || mShowUncompletedOnly) &&
                    (!cmd.getType().equals(Command.CommandType.FILTER) && !cmd.getType().equals(Command.CommandType.HELP))) {
                removeFilter();
            }

            mProcessor.processCommand(cmd);

            // for help command, display help
            if (cmd.getType().equals(Command.CommandType.HELP)) {
                mDisplayHelp.setValue(true);
            } else {
                mDisplayHelp.setValue(false);
            }

            // for filter command, filter values
            if (cmd.getType().equals(Command.CommandType.FILTER)) {
                filterList(cmd);
            }
        } catch (ProcessingException e) {
            Log.e("MainViewModel", e.getMessage(), e);
            mMessage.setValue(e.getMessage());
            return;
        }
    }

    LiveData<List<TodoItem>> getItems() {
        return mItems;
    }

    LiveData<String> getMessage() {
        return mMessage;
    }

    LiveData<Boolean> getDisplayHelp() {
        return mDisplayHelp;
    }


    // ================================
    // Private methods
    // ================================

    private void filterList(Command cmd) {
        if (cmd.getData().toLowerCase().equals("uncompleted")) {
            mShowUncompletedOnly = true;
            mShowCompletedOnly = false;
        } else if (cmd.getData().toLowerCase().equals("completed")) {
            mShowUncompletedOnly = false;
            mShowCompletedOnly = true;
        } else if (cmd.getData().toLowerCase().equals("remove")) {
            removeFilter();
        }
        refreshData();
    }

    private void removeFilter() {
        if (mShowCompletedOnly || mShowUncompletedOnly) {
            mShowCompletedOnly = mShowUncompletedOnly = false;
            refreshData();
        }
    }

    private void refreshData() {
        mItems.removeSource(mSource);
        mItems.addSource(mRepo.getItems(), todoItems -> {
            if (todoItems != null && (mShowCompletedOnly || mShowUncompletedOnly)) {
                List<TodoItem> filteredList = new ArrayList<>(todoItems.size());
                for (TodoItem item : todoItems) {
                    if (mShowCompletedOnly && item.isCompleted()) {
                        filteredList.add(item);
                    } else if (mShowUncompletedOnly && !item.isCompleted()) {
                        filteredList.add(item);
                    }
                }
                mItems.setValue(filteredList);
                return;
            }
            mItems.setValue(todoItems);
        });
    }
}

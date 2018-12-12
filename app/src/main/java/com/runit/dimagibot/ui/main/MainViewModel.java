package com.runit.dimagibot.ui.main;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.runit.dimagibot.command.ICommandProcessor;
import com.runit.dimagibot.command.ProcessingException;
import com.runit.dimagibot.data.ITodoRepository;
import com.runit.dimagibot.data.model.TodoItem;
import com.runit.dimagibot.utils.SingleLiveEvent;

import java.util.List;

public class MainViewModel {

    private final ICommandProcessor mProcessor;
    private final ITodoRepository mRepo;
    private SingleLiveEvent<String> mMessage = new SingleLiveEvent<>();

    public MainViewModel(ICommandProcessor processor, ITodoRepository repository) {
        mProcessor = processor;
        mRepo = repository;
    }

    // ================================
    // Public API
    // ================================

    void onNewCommand(String command) {
        try {
            mProcessor.processCommand(command);
        } catch (ProcessingException e) {
            Log.e("MainViewModel", e.getMessage(), e);
            mMessage.setValue(e.getMessage());
            return;
        }
    }

    LiveData<List<TodoItem>> getItems() {
        return mRepo.getItems();
    }

    LiveData<String> getMessage() {
        return mMessage;
    }
}

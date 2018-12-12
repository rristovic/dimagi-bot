package com.runit.dimagibot.data;

import android.arch.lifecycle.LiveData;

import com.runit.dimagibot.data.model.TodoItem;

import java.util.List;

public interface ITodoRepository {

    void insert(String data);

    void delete(int indx);

    void update(int indx, String data);

    void markDone(int indx);

    LiveData<List<TodoItem>> getItems();
}

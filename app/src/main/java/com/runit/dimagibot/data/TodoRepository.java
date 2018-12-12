package com.runit.dimagibot.data;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runit.dimagibot.data.model.TodoItem;
import com.runit.dimagibot.utils.Executor;

import java.util.ArrayList;
import java.util.List;

public class TodoRepository implements ITodoRepository {

    private static final String KEY_ITEMS = "data";
    private final SharedPreferences mSharedPrefs;
    private final Executor mExecutor;

    private List<TodoItem> mData = new ArrayList<>();
    private MutableLiveData<List<TodoItem>> mDataSource = new MutableLiveData<>();

    private final Object mDataLock = new Object();

    public TodoRepository(SharedPreferences sharedPreferences, Executor executor) {
        mSharedPrefs = sharedPreferences;
        mExecutor = executor;
        getData();
    }

    // ================================
    // Public API
    // ================================

    @Override
    public void insert(String data) {
        mExecutor.execute(() -> {
            synchronized (mDataLock) {
                mData.add(new TodoItem(data));
                postNewData();
            }
        });
    }

    @Override
    public void delete(int indx) {
        mExecutor.execute(() -> {
            synchronized (mDataLock) {
                if (mData.size() == 0 || mData.size() <= indx) return;
                mData.remove(indx);
                postNewData();
            }
        });
    }

    @Override
    public void update(int indx, String data) {
        mExecutor.execute(() -> {
            synchronized (mDataLock) {
                if (mData.size() == 0 || mData.size() <= indx) return;
                mData.set(indx, new TodoItem(data));
                postNewData();
            }
        });
    }

    @Override
    public void markDone(int indx) {
        mExecutor.execute(() -> {
            synchronized (mDataLock) {
                if (mData.size() == 0 || mData.size() <= indx) return;
                mData.set(indx, mData.get(indx).setCompleted(true));
                postNewData();
            }
        });
    }

    @Override
    public LiveData<List<TodoItem>> getItems() {
        return mDataSource;
    }

    // ================================
    // Private methods
    // ================================

    private void getData() {
        mExecutor.execute(() -> {
            synchronized (mDataLock) {
                String jsonData = mSharedPrefs.getString(KEY_ITEMS, null);
                // assert data exists
                if (jsonData == null) return;

                mData = new Gson().fromJson(jsonData, new TypeToken<List<TodoItem>>() {
                }.getType());
                postNewData();
            }
        });
    }

    @SuppressLint("ApplySharedPref")
    private void postNewData() {
        mDataSource.postValue(mData);
        mExecutor.execute(() -> mSharedPrefs.edit()
                .putString(KEY_ITEMS, new Gson().toJson(mData))
                .commit());
    }
}

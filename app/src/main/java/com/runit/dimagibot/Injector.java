package com.runit.dimagibot;

import android.content.Context;
import android.content.SharedPreferences;

import com.runit.dimagibot.command.CommandHandler;
import com.runit.dimagibot.command.CommandProcessor;
import com.runit.dimagibot.command.ICommandHandler;
import com.runit.dimagibot.command.ICommandProcessor;
import com.runit.dimagibot.data.ITodoRepository;
import com.runit.dimagibot.data.TodoRepository;
import com.runit.dimagibot.ui.main.MainViewModel;
import com.runit.dimagibot.utils.Executor;
import com.runit.dimagibot.utils.JobExecutor;

public class Injector {
    private static Executor mExecutor;
    private static SharedPreferences mPrefs;
    private static ITodoRepository mRepo;

    static void init(Context context) {
        mExecutor = new JobExecutor();
        mPrefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        mRepo = new TodoRepository(mPrefs, mExecutor);
    }

    // =========================
    // Singleton scopes
    // =========================
    public static ITodoRepository injectTodoRepository() {
        return mRepo;
    }

    public static Executor injectExecutorService() {
        return mExecutor;
    }


    // =========================
    // Prototype scopes
    // =========================
    public static ICommandProcessor injectCommandProcessor() {
        return new CommandProcessor(injectCommandHandler());
    }

    public static ICommandHandler injectCommandHandler() {
        return new CommandHandler(injectTodoRepository());
    }

    public static MainViewModel injectMainViewModel() {
        return new MainViewModel(injectCommandProcessor(), injectTodoRepository());
    }
}

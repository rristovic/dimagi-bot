package com.runit.dimagibot.utils;

public interface Executor {

    /**
     * Method for executing provided task in background thread.
     *
     * @param command {@link Runnable} task to be executed.
     */
    void execute(Runnable command);

    /**
     * Method for executing provided task on Main/UI thread.
     *
     * @param command {@link Runnable} task to be executed.
     */
    void executeOnMain(Runnable command);
}

package com.runit.dimagibot.data.model;

public class TodoItem {

    private String content;
    private boolean isCompleted;

    public TodoItem(String content) {
        this(content, false);
    }

    public TodoItem(String content, boolean isCompleted) {
        this.content = content;
        this.isCompleted = isCompleted;
    }

    public TodoItem setCompleted(boolean isCompleted) {
        return new TodoItem(this.content, isCompleted);
    }

    public String getContent() {
        return content;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}

package com.runit.dimagibot.command;

public class Command {

    private CommandType type;
    private String data;

    public Command(CommandType type, String data) {
        this.type = type;
        this.data = data;
    }

    public CommandType getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public enum  CommandType {
        ADD("add"), REMOVE("remove"), EDIT("edit"), MARK_DONE("completed"), REMINDER("remind"), HELP("help"), FILTER("filter");

        public final String rawText;

        CommandType(String rawText) {
            this.rawText = rawText;
        }
    }
}

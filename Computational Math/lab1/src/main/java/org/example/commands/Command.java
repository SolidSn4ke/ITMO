package org.example.commands;

public interface Command {
    void execute(String[] args);

    String description();
}

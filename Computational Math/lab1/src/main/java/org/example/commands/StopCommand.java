package org.example.commands;

public class StopCommand implements Command{
    @Override
    public void execute(String[] args) {
        System.exit(0);
    }

    @Override
    public String description() {
        return "stop - завершение работы программы";
    }
}

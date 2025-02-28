package org.example.commands;

import org.example.system.io.OutputManager;

public class HelpCommand implements Command{
    @Override
    public void execute(String[] args) {
        OutputManager om = new OutputManager();
        for (String str : args){
            om.print(str);
        }
    }

    @Override
    public String description() {
        return "help - список доступных команд";
    }
}

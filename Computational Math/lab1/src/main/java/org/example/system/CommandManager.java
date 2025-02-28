package org.example.system;

import org.example.commands.Command;
import org.example.commands.HelpCommand;
import org.example.commands.StartCommand;
import org.example.commands.StopCommand;

import java.util.Map;
import java.util.stream.Collectors;

public class CommandManager {
    private final Map<String, Command> commands = Map.ofEntries(Map.entry("start", new StartCommand()),
            Map.entry("help", new HelpCommand()), Map.entry("stop", new StopCommand()));

    public void invoke(String commandName, String[] args){
        if (commandName.equalsIgnoreCase("help")) args = commands.values().stream().map(Command::description).collect(Collectors.toSet()).toArray(new String[commands.size()]);
        commands.get(commandName.toLowerCase()).execute(args);
    }

    public boolean checkCommand(String commandName){
        return commands.containsKey(commandName.toLowerCase());
    }
}

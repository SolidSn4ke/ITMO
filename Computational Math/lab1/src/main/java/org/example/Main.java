package org.example;

import org.example.system.CommandManager;
import org.example.system.io.InputManager;
import org.example.system.io.OutputManager;

public class Main {
    public static void main(String[] args) {
        InputManager im = new InputManager();
        CommandManager cm = new CommandManager();
        OutputManager om = new OutputManager();

        om.print("Программа запущена. Чтобы посмотреть список команд введите help");

        while (true) {
            om.messageInputPrompt();
            String input = im.tryReadLine();
            String[] listOfArguments = input.strip().split(" +");

            if (cm.checkCommand(listOfArguments[0])) {
                cm.invoke(listOfArguments[0], listOfArguments);
            } else om.print("Такой команды нет. Чтобы посмотреть список команд введите help");
        }
    }
}
package org.example.system.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputManager {
    private Scanner scanner = new Scanner(System.in);

    public boolean changeSource(File path) {
        if (path != null) {
            try {
                scanner = new Scanner(path);
                return true;
            } catch (FileNotFoundException ignored) {
                return false;
            }
        } else return false;
    }

    public String tryReadLine(){
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e){
            changeSourceToStdIn();
            return null;
        }
    }

    public void changeSourceToStdIn(){
        scanner = new Scanner(System.in);
    }
}

package org.example.system.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputManager {
    public void print(Object obj){
        System.out.println(obj);
    }

    public void messageInputPrompt(){System.out.print("-> ");}

    public boolean printToFile(File path, Object obj){
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(obj.toString());
            return true;
        } catch (IOException e) {
            print("File not found");
            return false;
        }
    }
}

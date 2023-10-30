import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
class parser {
    String commandName;
    String args[];

    public boolean parse(String input) {
        String temp[] = input.split("\\s+");
        this.commandName = temp[0];
        args = new String[temp.length - 1];
        for (int i = 1; i < temp.length; ++i) {
            args[i - 1] = temp[i].trim();
        }
        return true;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }

}

public class Terminal {
    static parser parser;

    // commands
    public static void echo(String[] args) {
        if (args.length < 1) {
            System.out.println("invalid argument");

        } else {
            for (int i = 0; i < args.length; ++i) {
                System.out.print(args[i] + " ");

            }
            System.out.println();
        }
    }

    public static void pwd(String[] args) {
        if (args.length != 0) {
            System.out.println("invalid argument");

        } else {
            System.out.println(System.getProperty("user.dir"));
        }
    }

    public static void rm(String[] args) {
        if (args.length != 1) {
            System.out.println("invalid argument");

        } else {
            String fileName = args[0];
            fileName += ".txt";
            File f = new File(fileName);
            if (!f.delete()) {
                System.out.println("file not found");
            }
        }
    }
    public static void ls() {
        // Get the current directory path
        File file = new File(System.getProperty("user.dir"));

        if (!file.exists()) {
            System.out.println("This directory does not exist.");
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        System.out.println("Directory: " + f.getName());
                    } else {
                        System.out.println("File: " + f.getName());
                    }
                }
            } else {
                System.out.println("No files or directories in this directory.");
            }
        }
    }


     public static void touch(String [] fileNames) {
        for (String f : fileNames) {
            File file = new File(f);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    System.out.println("Created file:" + f);
                } catch (IOException e) {
                    System.err.println("Error reading the file: " + e.getMessage());
                }

            }
            //if file is exist modification time of the file
            else {
                file.setLastModified(System.currentTimeMillis());
                System.out.println("update time of file: "+f);
            }
        }
    }
    

    public static void cat(String[] fileNames) {
        for (String f : fileNames) {
            File file = new File(f);
            if (file.exists() && file.isFile()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line = br.readLine();
                    while (line != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading the file: " + e.getMessage());
                }
            }
             else
                System.out.println("file not exist!!"+f);
        }
    }
    public static void chooseCommandAction(String command) {
        switch (command) {
            case "echo":
                echo(parser.getArgs());
                break;
            case "pwd":
                pwd(parser.getArgs());
                break;
            case "rm":
                rm(parser.getArgs());
                break;
            case "cat":
                cat(parser.getArgs());
                break;
            case "touch":
                touch(parser.getArgs());
                break;
            case "ls":
                ls();
                break;
            default:
                System.out.println("command not found");
                break;
        }
    }

    // to keep the working dir on screen
    public static void printCurrentDir() {
        System.out.print(System.getProperty("user.dir") + " >");
    }

    public static void main(String[] args) {
        parser = new parser();
        printCurrentDir();

        // taking first command
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        parser.parse(input);

        while (true) {
            if (parser.getCommandName().toLowerCase().equals("exit")) {
                break;
            } else {
                chooseCommandAction(parser.getCommandName());
                printCurrentDir();
                input = scan.nextLine();
                parser.parse(input);
            }
        }

    }
}

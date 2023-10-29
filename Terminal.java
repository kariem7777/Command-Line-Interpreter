import java.util.*;
import java.io.File;

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
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;

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

    public static void touch(String[] fileNames) {
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
            // if file is exist modification time of the file
            else {
                file.setLastModified(System.currentTimeMillis());
                System.out.println("update time of file: " + f);
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
            } else
                System.out.println("file not exist!!" + f);
        }
    }

    public static void lsR(String[] args) {
        if (args.length == 1 && args[0].equals("-r")) {

            File currentDir = new File(System.getProperty("user.dir"));
            File[] files = currentDir.listFiles();
            if (files != null) {
                Arrays.sort(files, Collections.reverseOrder());
                for (File file : files) {
                    if (file.isDirectory()) {
                        System.out.println(file.getName() + "/");
                    } else {
                        System.out.println(file.getName());
                    }
                }
            }
        } else
            System.out.println("invalid argument");
    }

    public static void cp(String[] args) {

        String sourceFilePath = args[0];
        String destinationFilePath = args[1];
        File sourceFile = new File(sourceFilePath);
        File destinationFile = new File(destinationFilePath);

        if (sourceFile.isFile() && destinationFile.isFile()) {
            try {
                Files.copy(Paths.get(sourceFilePath), Paths.get(destinationFilePath),
                        StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File copied successfully.");
            } catch (IOException e) {
                System.out.println("Error copying the file: " + e.getMessage());
            }
        } else {
            System.out.println(" source and destination must be files.");
        }
    }

    public static void cpR(String[] args) {
        if (args.length == 3 && args[0].equals("-r")) {

            String sourceDirectoryPath = args[1];
            String destinationDirectoryPath = args[2];

            File sourceDirectory = new File(sourceDirectoryPath);
            File destinationDirectory = new File(destinationDirectoryPath);

            if (sourceDirectory.isDirectory() && destinationDirectory.isDirectory()) {
                try {
                    copyDirectory(sourceDirectory, destinationDirectory);
                    System.out.println("Directory copied successfully.");
                } catch (IOException e) {
                    System.out.println("Error copying the directory: " + e.getMessage());
                }
            } else {
                System.out.println(" source and destination must be directories.");
            }
        } else {
            System.out.println("invalid arguments");
        }
    }

    private static void copyDirectory(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }

            String[] files = source.list();
            if (files != null) {
                for (String file : files) {
                    File srcFile = new File(source, file);
                    File destFile = new File(destination, file);

                    copyDirectory(srcFile, destFile);
                }
            }
        }
    }

    public static String handlePath(String[] args) {
        if (args[0].matches("^[a-zA-Z][:].*")) {
            return args[0];
        } else {
            String Pth = System.getProperty("user.dir");
            Pth += "\\" + args[0];
            return Pth;
        }
    }

    public static void cd(String[] Dir) {

        if (Dir.length == 0) {
            System.setProperty("user.dir", "C:\\Users");

        } else if (Dir.length == 1) {

            if (Dir[0].equals("..")) {
                String tmpPath = System.getProperty("user.dir");
                File file = new File(tmpPath);
                tmpPath = file.getParent().toString();
                System.setProperty("user.dir", tmpPath);
            } else {
                String dirc = handlePath(Dir);
                File f = new File(dirc);
                if (f.exists()) {
                    System.setProperty("user.dir", dirc);
                } else {
                    System.out.println("invalid path");
                }

            }
        } else {
            System.out.println("invalid argument");
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
            case "lsR":
                lsR(parser.getArgs());
                break;
            case "cp":
                if (parser.args.length == 3)
                    cpR(parser.getArgs());
                else if (parser.getArgs().length == 2)
                    cp(parser.getArgs());
                else
                    System.out.println("invalid argument");
                break;
            case "cd":
                cd(parser.getArgs());
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

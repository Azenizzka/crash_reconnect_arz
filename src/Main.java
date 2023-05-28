import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final boolean[] found = new boolean[1];
    private static boolean isActive = false;

    public static void main(String[] args) throws IOException {
        isActive = true;
        begin("gta_sa.exe");
    }

    public static void resetValues() {
        found[0] = false;
    }

    public static void begin(String processToSearch) throws IOException {
        while (isActive){
            Process process = new ProcessBuilder("tasklist.exe", "/fo", "csv", "/nh").start();
            Scanner sc = new Scanner(process.getInputStream());
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(",");
                String processName = parts[0].substring(1).replaceFirst(".$", "");
                if(processName.equalsIgnoreCase(processToSearch)) {
                    found[0] = true;
                }
            }

            try {
                process.waitFor();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            if(found[0]) {
                System.out.println(Colors.ANSI_GREEN + "Был найден процесс " + Colors.ANSI_CYAN + processToSearch + Colors.ANSI_RESET);
                try {
                    checkCrash();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println(Colors.ANSI_RED + "Процесс не был найден " + Colors.ANSI_CYAN + processToSearch + Colors.ANSI_RESET);
                Launch.samp("Jamal_Blackberry", "80.66.82.168", "7777");
            }
            resetValues();
        }
    }

    public static void checkCrash() throws IOException {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            String data = (String) clipboard.getData(DataFlavor.stringFlavor);
            System.out.println(Colors.ANSI_RED + "CLIPBOARD: " + Colors.ANSI_WHITE + '\"' + Colors.ANSI_CYAN + data + Colors.ANSI_WHITE + '\"' + Colors.ANSI_RESET);
            if(data.startsWith("https://crash.sr.team/?uid=")) {
                clearClipBoard();
                killProcess("gta_sa.exe");
            }
        } catch (UnsupportedFlavorException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void clearClipBoard() {
        StringSelection stringSelection = new StringSelection("RELOAD SAMP");
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    static int i;
    static{
        i = 47;
    }


    public static void killProcess(String process) throws IOException {
        Process exec = Runtime.getRuntime().exec("taskkill /F /T /im " + process);
        System.out.println(Colors.ANSI_RED + "KILL PROCESS: " + Colors.ANSI_PURPLE + process + Colors.ANSI_RESET);
    }
}
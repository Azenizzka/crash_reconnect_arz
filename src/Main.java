import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;

public class Main {
    private static final boolean[] found = new boolean[1];
    private static boolean isActive = false;

    private static int mode = 1; // 0 - auto    1 - time

    private static int minutesToJoin = 58; // minutes, while game must be started
    private static int minutesToExit = 2; // minutes, while game must be shutdown

    private static String process = "gta_sa.exe";

    private static String nickname = "Sam_Mason";
    private static String serverIp = "80.66.82.168";
    private static String serverPort = "7777";

    private static int checkDelay = 5;
    private static int doDelay = 120;

    public static void main(String[] args) throws IOException, InterruptedException {
        isActive = true;
        begin(process);
    }

    public static void resetValues() {
        found[0] = false;
    }

    public static void begin(String processToSearch) throws IOException, InterruptedException {
        while (isActive){
            if(mode == 0) {
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
                    Launch.samp(nickname, serverIp, serverPort);
                }
                resetValues();
            } else if(mode == 1) {
                Date date = new Date();
                System.out.println("time: " + date.getMinutes());
                Thread.sleep(checkDelay * 1000);

                if(date.getMinutes() == minutesToJoin) {
                    Launch.samp(nickname, serverIp, serverPort);
                    Thread.sleep(doDelay * 1000);
                } else if(date.getMinutes() == minutesToExit) {
                    killProcess(process);
                    Thread.sleep(doDelay * 1000);
                }
            }
        }
    }

    public static void checkCrash() throws IOException {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            String data = (String) clipboard.getData(DataFlavor.stringFlavor);
            System.out.println(Colors.ANSI_RED + "CLIPBOARD: " + Colors.ANSI_WHITE + '\"' + Colors.ANSI_CYAN + data + Colors.ANSI_WHITE + '\"' + Colors.ANSI_RESET);
            if(data.startsWith("https://crash.sr.team/?uid=")) {
                clearClipBoard();
                killProcess(process);
            }
        } catch (UnsupportedFlavorException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void clearClipBoard() {
        StringSelection stringSelection = new StringSelection("RELOAD SAMP");
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    public static void killProcess(String process) throws IOException {
        Process exec = Runtime.getRuntime().exec("taskkill /F /T /im " + process);
        System.out.println(Colors.ANSI_RED + "KILL PROCESS: " + Colors.ANSI_PURPLE + process + Colors.ANSI_RESET);
    }
}
import java.io.IOException;
import java.util.Scanner;

public class FindProcessThread extends Thread {
    private static boolean[] found;

    @Override
    public void run() {
        String processToSearch = "gta_sa.exe";
        Process process = null;
        try {
            process = new ProcessBuilder("tasklist.exe", "/fo", "csv", "/nh").start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        if(found[0]) {
            System.out.println(Colors.ANSI_GREEN + "Был найден процесс " + Colors.ANSI_CYAN + processToSearch + Colors.ANSI_RESET);
            try {
                Main.checkCrash();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(Colors.ANSI_RED + "Процесс не был найден " + Colors.ANSI_CYAN + processToSearch + Colors.ANSI_RESET);
            Launch.samp("Jamal_Blackberry", "80.66.82.168", "7777");
        }

        resetValues();
    }

    public static void resetValues() {
        found[0] = false;
    }
}

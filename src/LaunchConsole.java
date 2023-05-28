public class LaunchConsole {
    private static Process proc;
    public static void main(String args[]) {
        try {
            String[] cmd = new String[3];
            cmd[0] = "cmd.exe" ;
            cmd[1] = "/C" ;
            cmd[2] = args[0];

            Runtime rt = Runtime.getRuntime();
            System.out.println("Execing " + cmd[0] + " " + cmd[1] + " " + cmd[2]);
            proc = rt.exec(cmd);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static Process getProcess() {
        return proc;
    }
}
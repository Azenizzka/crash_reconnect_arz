public class Launch {
    public static void samp(String nickname, String ip, String port) {
        LaunchConsole.main(new String[] {"cd \"C:\\Games\\Arizona Games Launcher\\bin\\arizona\\\"" +
                " && " +
                "\"gta_sa.exe\" " +
                "-c -h " +
                ip +
                " -p " +
                port +
                " -n " +
                nickname +
                " -mem 2048 " +
                "-window " +
                "-x " +
                "-widescreen " +
                "-z ****** " +
                "-ldo " +
                "-arizona " +
                "-enable_skygfx"});
    }
}

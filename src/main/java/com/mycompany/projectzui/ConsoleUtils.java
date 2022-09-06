package com.mycompany.projectzui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.SwingWorker;

public class ConsoleUtils {

    private static String ttyConfig;

    public static void setNonBlockingMode() throws IOException, InterruptedException {

        ttyConfig = stty("-g");

        // set the console to be character-buffered instead of line-buffered
        stty("-icanon min 1");

        // disable character echoing
        stty("-echo");
    }

    public static void resetToDefaultMode() throws IOException, InterruptedException {

        ttyConfig = stty("sane");

    }

    /**
     * Execute the stty command with the specified arguments against the current
     * active terminal.
     */
    private static String stty(final String args)
            throws IOException, InterruptedException {
        String cmd = "stty " + args + " < /dev/tty";

        return exec(new String[]{
            "cmd",
            "-c",
            cmd
        });
    }

    /**
     * Execute the specified command and return the output (both stdout and
     * stderr).
     *
     * @param cmd each element of array will be taken as each argument E.g. ls
     * -l exec("ls", "-l")
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static String exec(String... cmd) throws IOException, InterruptedException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        Process p = Runtime.getRuntime().exec(cmd);
        int c;
        InputStream in = p.getInputStream();

        while ((c = in.read()) != -1) {
            bout.write(c);
        }

        in = p.getErrorStream();

        while ((c = in.read()) != -1) {
            bout.write(c);
        }

        p.waitFor();

        String result = new String(bout.toByteArray());
        return result;
    }

}

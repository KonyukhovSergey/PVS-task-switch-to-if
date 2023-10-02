package pvs;

import pvs.task.SwitchProcessor;
import spoon.Launcher;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {
    private static final String ARG_NAME_FOR_INPUT = "--ARG_NAME_FOR_INPUT";
    private static final String ARG_NAME_FOR_OUTPUT = "--ARG_NAME_FOR_OUTPUT";

    private static String getOptionValue(String[] args, String option) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(option)) {
                return args[i + 1];
            }
        }
        System.out.println("there is no option: `" + option + "` in the program");
        return null;
    }

    public static void main(String[] args) {
        final var launcher = new Launcher();
        launcher.setArgs(new String[]{
                "-i", getOptionValue(args, ARG_NAME_FOR_INPUT),
                "-o", getOptionValue(args, ARG_NAME_FOR_OUTPUT)
        });
        launcher.addProcessor(new SwitchProcessor());
        launcher.getEnvironment().setComplianceLevel(18);
        launcher.run();
    }
}
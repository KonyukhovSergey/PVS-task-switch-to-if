package ru.pvs.task;

import pvs.task.TestSwitch;
import spoon.Launcher;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final var expected = new TestSwitch().runTests();

        System.out.println(expected);

        System.out.println("\n\n\n");

        final String[] largs = {
                "-i", "src/test/java/pvs/task/TestSwitch.java",
                "-o", "target/spooned/",
                "-p", "ru.pvs.task.SwitchProcessor",
                "--compile"
        };

        final Launcher launcher = new Launcher();
        launcher.setArgs(largs);
        launcher.run();


        final var cl = loadSpoonedClass();
        final var clInstance = cl.getConstructor().newInstance();
        final var actual = cl.getMethod("runTests").invoke(clInstance);

        System.out.println(actual);

        org.junit.Assert.assertEquals(expected, actual);

    }

    public static Class loadSpoonedClass() {
        final var file = new File("spooned-classes");
        try {
            final var classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});
            return classLoader.loadClass("pvs.task.TestSwitch2");
        } catch (MalformedURLException | ClassNotFoundException e) {
            return null;
        }
    }
}
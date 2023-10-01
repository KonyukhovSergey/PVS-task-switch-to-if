package pvs.task;


import org.junit.Assert;
import org.junit.Test;
import pvs.task.switches.TestSwitch;
import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

public class TestSwitchRunner {

    @Test
    public void test() {
        try {

            Launcher launcher = new Launcher();
            launcher.setArgs(new String[]{"--compile"});
            launcher.getEnvironment().setComplianceLevel(18);
            launcher.addProcessor(new SwitchProcessor());
            launcher.addProcessor(new AbstractProcessor<CtClass<?>>() {
                @Override
                public void process(CtClass<?> ctClass) {
                    ctClass.setSimpleName(ctClass.getSimpleName() + "Processed");
                }
            });
            launcher.addInputResource("src/main/java/pvs/task/switches/TestSwitch.java");
            launcher.setSourceOutputDirectory("target/spooned");
            launcher.run();

            final var actualClass = TestSwitch.class.getConstructor().newInstance();

            URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("spooned-classes").toURI().toURL()});
            final var spoonedClass = classLoader
                    .loadClass("pvs.task.switches.TestSwitchProcessed")
                    .getConstructor()
                    .newInstance();

            for (var methodName : getTestMethods(TestSwitch.class)) {
                Assert.assertEquals(
                        methodName,
                        actualClass.getClass().getMethod(methodName).invoke(actualClass),
                        spoonedClass.getClass().getMethod(methodName).invoke(spoonedClass));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private List<String> getTestMethods(Class<?> cl) {
        return Arrays.stream(cl.getDeclaredMethods())
                .filter(method -> method.getParameterCount() == 0)
                .filter(method -> method.getReturnType() == String.class)
                .filter(method -> method.getName().startsWith("test"))
                .map(method -> method.getName())
                .toList();
    }

}

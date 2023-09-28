package ru.pvs.task;

import spoon.Launcher;

public class Main {
    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            System.out.println("--- sw for " + i + " ---");

            switch (i) {
                default:
                    System.out.println("default");

                case 1:
                    System.out.println("one");
                    break;
                case 2:
                case 3:
                    System.out.println("two or three");
                case 4:
                    System.out.println("three");
                    break;
            }
        }

        final String[] largs = {
                "-i", "src/test/java/pvs/task/TestSwitch.java",
                "-o", "target/spooned/",
                "-p", "ru.pvs.task.SwitchProcessor",
                "--compile"
        };

        final Launcher launcher = new Launcher();
        launcher.setArgs(largs);
        launcher.run();

//        var l = Launcher.parseClass(readFileAsText("./src/main/java/ru/pvs/task/TestSwitches.java"));
//
//
//        var mutator = new SwitchProcessor();
//
//        mutator.process();
//
//        l.getMethods().forEach(m -> m.getBody()
//                .addComment(l.getFactory().createInlineComment("this is method: " + m.getSimpleName())));
//
//        l.getMethods().forEach(m -> m.accept(new CtScanner() {
//            @Override
//            public <S> void visitCtSwitch(CtSwitch<S> switchStatement) {
//                super.visitCtSwitch(switchStatement);
//            }
//
//            @Override
//            public <T, S> void visitCtSwitchExpression(CtSwitchExpression<T, S> switchExpression) {
//                super.visitCtSwitchExpression(switchExpression);
//            }
//        }));
//
//
        System.out.println("Hello world! ");
    }
}
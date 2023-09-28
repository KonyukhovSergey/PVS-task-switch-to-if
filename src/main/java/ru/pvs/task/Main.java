package ru.pvs.task;

import spoon.Launcher;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            System.out.println("--- sw for " + i + " ---");
            switch (i) {
                case 7:
                    System.out.println("7 continue");
                    continue;
                default: {
                    System.out.println("default");
                }
                case 1:{
                    System.out.println("one");
                    break;}
                case 2:
                case 3:
                    System.out.println("two or three");
                case 4:
                    System.out.println("four");
                    break;
            }
        }


       // for(int var1 = 0; var1 < 10; ++var1) {
        var var1 = 2;
            System.out.println("--- sw for " + var1 + " ---");
            if (var1 == 1) {
                System.out.println("one");
            }

            if (var1 != 3) {
                if (var1 != 1 && var1 != 3 && var1 != 4 && var1 != 5 && var1 != 2) {
                    System.out.println("default");
                }

                if (var1 == 4) {
                    System.out.println("two or three");
                } else {
                    if (var1 == 5) {
                    }

                    if (var1 == 2) {
                        System.out.println("four");
                    }
                }
            }
       // }

var i =2;
        java.lang.System.out.println(("--- sw for " + i) + " ---");
        do {
            if(i != 1 && i != 2 && i != 3 && i != 4) {
                java.lang.System.out.println("default");
            }
            if(Objects.equals(i, 1)) {
                java.lang.System.out.println("one");
                break;
            }
            if(Objects.equals(i, 2)) {
                ;
            }
            if(Objects.equals(i, 3)) {
                java.lang.System.out.println("two or three");
            }
            if(Objects.equals(i, 4)) {
                java.lang.System.out.println("four");
                break;
            }
        } while (false);

        Objects.equals(3,4);
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
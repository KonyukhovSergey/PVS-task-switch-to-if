package pvs.task;

import java.util.stream.Stream;

public class TestSwitch {

    private final StringBuilder sb = new StringBuilder();

    public String runTests() {
        sb.setLength(0);

        emptySwitchStatement(111);
        colonSwitchStatement();
        colonSwitchStatement2();

        stringSwitchStatementColon();

        return sb.toString();
    }

    private void log(String message) {
        sb.append("%s%n".formatted(message));
    }


    private int hhh() {
        log("hhh");
        return (int) System.currentTimeMillis();
    }

    private void emptySwitchStatement(int a) {
        //int a = 0;
        switch (hhh()) {
        }
    }

    private void colonSwitchStatement() {
        for (int i = 0; i < 10; i++) {
            log("--- sw for " + i + " ---");
            switch (i) {
                default:
                    log("default");
                case 1:
                    switch (i * 2) {
                        case 6:

                        case 7:
                            //efe
                        {
                            {
                                for (; ; ) {
                                    break;
                                }
                                i = 4;
                                //break;
                                //rg
                            }
                        }
                        break;        //greg
                    }
                    log("one");
                    break;
                case 2:
                case 3:
                    log("two or three");
                case 4:
                    log("four");
                    break;
            }
        }
    }

    public void stringSwitchStatementColon() {
        Stream.of("one", "two", "undef", "null - default", "", "three")
                .forEach(v ->
                        {
                            switch (v) {
                                case "one":
                                    log("1");
                                    break;
                                default:
                                    log("default");
                                case "undef":
                                    log("undef");
                                    break;
                                case "two":
                                    log("2");
                                case "three":
                                    log("3");
                                    break;
                                case "":
                                    log("empty");
                            }
                        }
                );
    }

    public void colonSwitchStatement2() {
        for (int i = 0; i < 10; i++) {
            log("--- sw for " + i + " ---");
            switch (i) {
                case 1:
                    log("one");
                case 3:
                    break;
                default:
                    log("default");
                case 4:
                    log("two or three");
                    break;
                case 5:
                case 2:
                    log("three");
                    break;
            }
        }
    }

    public int arrowSwitchStatement(int value) {
        int a = 0;
        switch (value) {
            case 1 -> {
                a = 10;
                a += 2;
            }
            default -> a = 100;
            case 2, 3 -> a = 20;
            case 4 -> a += 5;
        }
        return a;
    }


    public int method3(int value) {
        return switch (value) {
            default -> 100;
            case 1 -> {
                yield 10;
            }
            case 2, 3 -> 20;
            case 4 -> 25;
        };

    }

}

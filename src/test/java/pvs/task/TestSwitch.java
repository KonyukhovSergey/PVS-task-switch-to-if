package pvs.task;


import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestSwitch {

    private final StringBuilder sb = new StringBuilder();

    public String runTests() {
        sb.setLength(0);

        noBreaksCases();

//        emptySwitchStatement(111);
//        colonSwitchStatement();
//        colonSwitchStatement2();
//        stringSwitchStatementColon();
//
//        arrowSwitchStatement();
//
//        switchExpressionCase();
//
//        switchExpression2();

        //switchExpressionHard();

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

    public void arrowSwitchStatement() {
        for (int value = 0; value < 10; value++) {
            log("-- arrow switch for " + value + " --");
            switch (value) {
                case 1 -> {
                    log("one 10");
                    log("one +2");
                }
                default -> log("default 100");
                case 2, 3 -> log("2 or 3");
                case 4 -> log("4");
            }
        }
    }

    public void noBreaksCases() {
        Stream.of("1", "2", "3", "100").forEach(arg -> {

            switch (arg) {
                default: {
                    log("default");
                }
                case "1": {
                    log("1");
                }
                case "2": {
                    log("2");
                }
                case "3": {
                    log("3");
                }
            }

            switch (arg) {

                case "1": {
                    log("1");
                }
                case "2": {
                    log("2");
                }
                default: {
                    log("default");
                }
                case "3": {
                    log("3");
                }
            }
        });
    }

    public String onlyDefaultSwitchExpression(String value) {
        return switch (value) {
            default -> "kukue " + value;
        };
    }

    public int switchExpression(int value) {
        return //aaa
                switch (value) {
                    default -> 100;
                    case 1 -> {
                        log("143");
                        yield switch (value * 2) {
                            case 0 -> 1;
                            default -> 1;

                        };
                    }
                    case 2, 3 -> 20;
                    case 4 -> {
                        {
                            var a = switch (value) {
                                case 2 -> {
                                    log("two");
                                    yield 2;
                                }
                                default -> 12;
                            };

                            log("asd " + a);
                        }
                        { //
                            {
                                {
                                    {
                                        /*wwqd*/
                                    }
                                }
                                //
                                yield 234;//
                            }
                        }
                    }
                };
    }


    public void switchExpressionCase() {
        IntStream.range(0, 10).forEach(i -> log("for " + i + " switch returns " + switchExpression(i)));
    }

    public void switchExpression2() {
        log("aaaaa");

        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).forEach(i -> log("aaaa " + i));

        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).forEach(i -> log("aaaa " + switch (i) {
            default -> 100;
            case 1 -> {
                yield 10;
            }
            case 2, 3 -> 20;
            case 4 -> 25;
        }));
//
//        // в лямбдах ложка скипает свичи
//        IntStream.range(0, 10).forEach(i -> log("aaaa " + switch (i) {
//            default -> 100;
//            case 1 -> {
//                yield 10;
//            }
//            case 2, 3 -> 20;
//            case 4 -> 25;
//        }));
    }

//    public void switchExpressionHard() {
//        IntStream.range(0, 10).map(i -> switch (i) {
//            default -> 100;
//            case 1 -> {
//                yield 10;
//            }
//            case 2, 3 -> 20;
//            case 4 -> 25;
//        }).forEach(i -> log("hard case " + i));
//    }

    // кажется оно в превью.
//    public void switchPatternMatching(){
//        final var items = new ArrayList<Object>();
//        items.add("aaaaaaaaaaaaaaa");
//        items.add(Integer.valueOf(100));
//        items.add(new ArrayList<Double>());
//
//        for(var item:items){
//            log(switch (item){
//                case Integer i -> "integer" + i;
//                default -> "a";
//
//            });
//        }
//    }

}

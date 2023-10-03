package pvs.task.switches;

import java.time.DayOfWeek;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

public class TestSwitch {


    // надо лямбда экпрешшен заменить на блок с ретурном
//    public String testSwitchExpressionInLyambda() {
//        return SwitchRunner.run((value, log) -> {
//            var outValue = (String) value;
//            IntStream.range(0, 10).mapToObj(i -> switch (i) {
//                default -> "outValue = " + outValue + " i = " + i;
//                case 1 -> {
//                    log.accept("jj");
//                    yield "10";
//                }
//                case 2, 3 -> "20";
//                case 4 -> "25";
//            }).forEach(i -> log.accept("hard case " + i));
//        }, 1, 2, 3, 4, 5);
//    }

    public String testSwitchExpressionString() {
        return SwitchRunner.run((value, log) -> {
            String v11 = (String) value;
            log.accept("sw expr for " + v11 + " is " + switch (v11 + v11) {
                case "one" -> "один";
                case "two" -> {
                    log.accept("in block");
                    yield "два (" + v11 + ")";
                }
                default -> v11 + " switched to default";
            });
        }, "one", "two", "three");
    }

    public String testEnumArrowSwitchStatement() {
        return SwitchRunner.run((value, log) -> {
            log.accept("-- enum arrow switch for " + value + " --");
            if (value == DayOfWeek.WEDNESDAY) {
                log.accept("its " + value + " my dudes!");
            } else {
                log.accept("wait for day of my dudes!");
            }
        }, DayOfWeek.FRIDAY, DayOfWeek.MONDAY, DayOfWeek.THURSDAY, DayOfWeek.WEDNESDAY);
    }

    public String testEmptyArrowSwitchStatement() {
        return SwitchRunner.run((value, log) -> {
            log.accept("-- arrow switch for " + value + " --");
            switch ((int) value + hello(log, "" + value).length()) {
            }
        }, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    public String testArrowSwitchStatement() {
        return SwitchRunner.run((value, log) -> {
            log.accept("-- arrow switch for " + value + " --");
            switch ((int) value) {
                case 1 -> {
                    log.accept("one 10");
                    log.accept("one +2");
                }
                default -> log.accept("default 100");
                case 2, 3 -> log.accept("2 or 3");
                case 4 -> log.accept("4");
            }
        }, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    public String testSwitchStatement() {
        return SwitchRunner.run((value, log) -> {
            switch ((int) value) {
                case 1:
                    log.accept("one");
                    break;
                case 2:
                    log.accept("two");
                    break;
                default:
                    log.accept("default");
                    break;
            }
        }, 1, 2, 3);
    }

    public String testSwitchStatementNoBreaks1() {
        return SwitchRunner.run((value, log) -> {
            switch ((int) value) {
                default:
                    log.accept("default");
                case 1:
                    log.accept("one");
                case 2:
                    log.accept("two");
            }
        }, 1, 2, 3);
    }

    public String testSwitchStatementNoBreaks2() {
        return SwitchRunner.run((value, log) -> {
            switch ((int) value) {
                case 1:
                    log.accept("one");
                case 2:
                    log.accept("two");
                default:
                    log.accept("default");
            }
        }, 1, 2, 3);
    }


    public String testSwitchStatementNoBreaks3() {
        return SwitchRunner.run((value, log) -> {
            switch ((int) value) {
                case 1:
                    log.accept("one");
                case 2:
                    log.accept("two");
                default:
            }
        }, 1, 2, 3);
    }

    public String testVariableSelector() {
        return SwitchRunner.run(((value, log) -> {
            int count = 0;
            final Function<Integer, Integer> f1 = v -> v + v;
            log.accept("for " + value);
            switch (f1.apply((int) value)) {
                case 1:
                    log.accept("ura! 1");
                    break;
                case 2:
                    log.accept("ura! 2");
                    break;
                case 3:
                    log.accept("ura! 3");
                    break;
                case 4:
                    log.accept("ura! 4");
                    break;
                default:
                    log.accept("no way");
                    break;
            }
        }), 1, 2, 3, 4);
    }

    private String hello(Consumer<String> a, String v) {
        a.accept("ttt " + v);
        return "v";
    }

    public String testEmptySwitchStatement() {
        return SwitchRunner.run(((value, log) -> {
            switch (hello(log, (String) value)) {

            }
        }), "one", "two");
    }

    public String testEmptySwitchStatement2() {
        return SwitchRunner.run(((value, log) -> {
            switch (value + "aqewfd") {
            }
        }), "one", "two");
    }

    public String testColonSwitchStatement() {
        return SwitchRunner.run(((value, log) -> {
            int i = (int) value;
            log.accept("switch for " + value);
            switch ((int) value) {
                default:
                    log.accept("default");
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
                    log.accept("one");
                    break;
                case 2:
                case 3:
                    log.accept("two or three");
                case 4:
                    log.accept("four");
                    break;

            }
        }), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    public String testStringColonSwitchStatement() {
        return SwitchRunner.run(((v, log) -> {
            switch ((String) v) {
                case "one":
                    log.accept("1");
                    break;
                default:
                    log.accept("default");
                case "undef":
                    log.accept("undef");
                    break;
                case "two":
                    log.accept("2");
                case "three":
                    log.accept("3");
                    break;
                case "":
                    log.accept("empty");
            }
        }), "one", "two", "undef", "null - default", "", "three");
    }

    public String testStringArrowSwitchStatement() {
        return SwitchRunner.run(((v, log) -> {
            switch ((String) v) {
                case "one" -> log.accept("1");
                default -> {
                    log.accept("default");
                    log.accept("default second line");
                }
                case "two" -> {
                    log.accept("fe");
                }
                case "three" -> log.accept("3");
            }
        }), "one", "two", "three", "four");
    }

}

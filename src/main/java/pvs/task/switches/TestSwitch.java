package pvs.task.switches;

import java.util.Arrays;
import java.util.function.Consumer;

public class TestSwitch {

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

    // починить!
//    public String testEmptySwitchStatement2() {
//        return SwitchRunner.run(((value, log) -> {
//            switch (1) {
//
//            }
//        }), "one", "two");
//    }

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

}

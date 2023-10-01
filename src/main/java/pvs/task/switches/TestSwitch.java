package pvs.task.switches;

import java.util.Arrays;

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

}

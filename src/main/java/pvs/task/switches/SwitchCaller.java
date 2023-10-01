package pvs.task.switches;

import java.util.function.Consumer;

public interface SwitchCaller {
    void execute(Object value, Consumer<String> log);
}

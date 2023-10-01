package pvs.task.switches;

import java.util.Arrays;

public class SwitchRunner {
    public static String run(SwitchCaller switchCaller, Object... values) {
        final var sb = new StringBuilder();
        Arrays.stream(values).forEach(value -> switchCaller.execute(
                value,
                message -> sb.append("%s%n".formatted(message))
        ));
        return sb.toString();
    }
}

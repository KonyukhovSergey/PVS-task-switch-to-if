package pvs.task;

import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtSwitch;

public class SwitchStatementBlockWrapper {
    private final int selectorVariableIndex;
    private final CtSwitch<?> sw;

    public SwitchStatementBlockWrapper(int selectorVariableIndex, CtSwitch<?> sw) {
        this.selectorVariableIndex = selectorVariableIndex;
        this.sw = sw;
    }

    public String getSelectorVariableName() {
        return "selectorValue%s".formatted(selectorVariableIndex);
    }

    public CtExpression<?> getSelectorVariable() {
        return sw.getFactory().createCodeSnippetExpression(getSelectorVariableName());
    }

    public CtBlock<Object> generate() {
        final var factory = sw.getFactory();
        final var block = factory.createBlock();
        block.addStatement(factory.createCodeSnippetStatement(
                "final var %s = %s".formatted(
                        getSelectorVariableName(),
                        sw.getSelector()
                )));
        return block;
    }
}

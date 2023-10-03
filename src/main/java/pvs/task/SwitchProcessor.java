package pvs.task;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

public class SwitchProcessor extends AbstractProcessor {

    @Override
    public boolean isToBeProcessed(CtElement candidate) {
        if (candidate instanceof CtMethod<?>) {
            selectorVariableIndex = 10000;
            switchExpressionIndex = 10000;
        }
        return candidate instanceof CtSwitch<?> || candidate instanceof CtSwitchExpression<?, ?>;
    }

    @Override
    public void process(CtElement ctElement) {
        if (ctElement instanceof CtSwitch<?>) {
            process((CtSwitch<?>) ctElement);
        } else if (ctElement instanceof CtSwitchExpression<?, ?>) {
            process((CtSwitchExpression<?, ?>) ctElement);
        }
    }

    private int selectorVariableIndex = 10000;
    private int switchExpressionIndex = 10000;

    private void process(CtSwitchExpression<?, ?> sw) {
        new SwitchExpressionToIfReplacer(switchExpressionIndex++, sw).replaceToIf();
//        final var switchMethodBodyGenerator = new SwitchExpressionToIfReplacer(switchExpressionIndex++, sw);
//        final var switchExpressionMethod = switchMethodBodyGenerator.generate();
//
//        sw.getParent(CtClass.class).addMethod(switchExpressionMethod);
//
//        sw.replace(sw.getFactory().createCodeSnippetExpression(switchMethodBodyGenerator.methodName + "(" + sw.getSelector() + ")"));

    }

    private void process(CtSwitch<?> sw) {
        if (sw.getCases().isEmpty()) {
            final var block = getFactory().createBlock();
            block.addStatement(getFactory().createCodeSnippetStatement("var tempVariableForSelector = " + sw.getSelector()));
            sw.replace(block);
        } else {
            sw.replace(new SwitchStatementGenerator(selectorVariableIndex++, sw).generate());
        }
    }

}

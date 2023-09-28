package ru.pvs.task;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtSwitch;
import spoon.reflect.code.CtSwitchExpression;
import spoon.reflect.declaration.CtElement;

import java.util.stream.Collectors;

public class SwitchProcessor extends AbstractProcessor {
    @Override
    public boolean isToBeProcessed(CtElement candidate) {
        return candidate instanceof CtSwitch<?> || candidate instanceof CtSwitchExpression<?, ?>;
    }

    @Override
    public void process(CtElement ctElement) {
        if (ctElement instanceof CtSwitch<?> sw) {
            process(sw);
        } else if (ctElement instanceof CtSwitchExpression<?, ?> sw) {
            process(sw);
        }
    }

    private void process(CtSwitchExpression<?, ?> sw) {
    }

    private void process(CtSwitch<?> sw) {
        if (sw.getCases().isEmpty()) {
            sw.replace(sw.getSelector());
        } else {
            final var snippet = getFactory().Core().createCodeSnippetStatement();
            snippet.setValue(switch (sw.getCases().get(0).getCaseKind()) {
                case ARROW -> getCodeForStatementArrowCase(sw);
                case COLON -> getCodeForStatementColonCase(sw);
            });
            sw.replace(snippet);
        }
    }

    private String getCodeForStatementArrowCase(CtSwitch<?> sw) {

        // if(v == e1) {stts1} else if (v == e2) {stts2}... else {stts3}

        return sw.getCases().stream()
                .map(swc -> "  if(" + sw.getSelector() + " == " + swc.getCaseExpression() +
                        swc.getStatements().stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(";\n    ", ") {\n    ", ";\n  }"))
                )
                .collect(Collectors.joining("\n", "do {\n", "\n} while (false)"));
    }

    private String getCodeForStatementColonCase(CtSwitch<?> sw) {
        return sw.getCases().stream()
                .map(swc ->
                        "  if(" + (swc.getCaseExpressions().isEmpty()
                                ? sw.getCases().stream()
                                .filter(dc -> !dc.getCaseExpressions().isEmpty())
                                .map(dc -> sw.getSelector() + " != " + dc.getCaseExpression())
                                .collect(Collectors.joining(" && "))
                                : sw.getSelector() + " == " + swc.getCaseExpression()
                        ) + ") " + swc.getStatements().stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(";\n    ", "{\n    ", ";\n  }"))
                )
                .collect(Collectors.joining("\n", "do {\n", "\n} while (false)"));
    }
}

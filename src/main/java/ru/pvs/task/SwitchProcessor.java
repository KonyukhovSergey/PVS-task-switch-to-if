package ru.pvs.task;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

            // it does not work...
            // snippet.setValue(snippet.prettyprint());

            sw.replace(snippet);
        }
    }

    private String getCodeForStatementArrowCase(CtSwitch<?> sw) {

        // if(v == e1) {stts1} else if (v == e2) {stts2}... else {stts3}

        return Stream.concat(
                sw.getCases().stream()
                        .filter(swc -> !swc.getCaseExpressions().isEmpty())
                        .map(swc -> swc.getCaseExpressions().stream()
                                .map(e -> sw.getSelector() + " == " + e.toString())
                                .collect(Collectors.joining(
                                        " || ",
                                        "if(",
                                        swc.getStatements().stream()
                                                .map(Objects::toString)
                                                .collect(Collectors.joining(";\n    ", ") {\n    ", ";\n  }")))
                                )
                        ),
                sw.getCases().stream()
                        .filter(swc -> swc.getCaseExpressions().isEmpty())
                        .map(swc -> swc.getStatements().stream()
                                .map(Objects::toString)
                                .collect(Collectors.joining(";\n    ", "{\n    ", ";\n  }"))
                        )
        ).collect(Collectors.joining("\n else\n "));
    }

    private String getIfExpression(CtCase<?> swc) {
        final var sw = (CtAbstractSwitch<?>) swc.getParent();
        return swc.getCaseExpressions().isEmpty()
                ? sw.getCases().stream()
                .filter(dc -> !dc.getCaseExpressions().isEmpty())
                .map(dc -> sw.getSelector() + " != " + dc.getCaseExpression())
                .collect(Collectors.joining(" && "))
                : sw.getSelector() + " == " + swc.getCaseExpression();
    }

    private boolean checkIfStatementsEndsWithBreak(List<CtStatement> statements) {
        int lastStatementIndex = statements.size() - 1;

        while (lastStatementIndex >= 0) {
            if (!(statements.get(lastStatementIndex) instanceof CtComment)) {
                break;
            }
            lastStatementIndex--;
        }

        if (lastStatementIndex < 0) {
            return false;
        }

        final var lastStatement = statements.get(lastStatementIndex);

        if (lastStatement instanceof CtBlock<?> ctBlock) {
            return checkIfStatementsEndsWithBreak(ctBlock.getStatements());
        }

        return lastStatement instanceof CtBreak;
    }

    private boolean checkIfCaseHaveBreak(CtCase<?> swc) {
        return checkIfStatementsEndsWithBreak(swc.getStatements());
    }

    private String getCodeForStatementColonCase(CtSwitch<?> sw) {
        final var expressions = new ArrayList<String>();
        final var sb = new StringBuilder();

        sw.getCases().forEach(swc -> {
            final var caseExpression = getIfExpression(swc);

            final var ifExpression = Stream.concat(expressions.stream(), Stream.of(caseExpression))
                    .collect(Collectors.joining(") || (", "(", ")"));

            sb
                    .append("if(")
                    .append(ifExpression)
                    .append(swc.getStatements().stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(";\n    ", ") {\n    ", ";\n  }")));

            if (checkIfCaseHaveBreak(swc)) {
                expressions.clear();
            } else {
                expressions.add(caseExpression);
            }
        });

        return sb.toString();

    }
}

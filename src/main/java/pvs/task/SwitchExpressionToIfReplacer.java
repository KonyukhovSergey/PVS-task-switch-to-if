package pvs.task;

import spoon.reflect.code.*;
import spoon.reflect.factory.Factory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SwitchExpressionToIfReplacer {
    private final String switchValue;
    private final String switchResult;

    private final CtSwitchExpression<?, ?> swe;
    private final Factory factory;

    public SwitchExpressionToIfReplacer(int switchIndex, CtSwitchExpression<?, ?> swe) {
        this.switchValue = "switchValue%s".formatted(switchIndex);
        this.switchResult = "switchResult%s".formatted(switchIndex);
        this.swe = swe;
        factory = swe.getFactory();
    }

    private CtExpression<Boolean> getIfArrowCaseExpression(CtExpression<?> selector, CtCase<?> swc) {
        return factory.createCodeSnippetExpression(swc.getCaseExpressions().stream()
                .map(e -> e.getType().isPrimitive()
                        ? selector + " == " + e
                        : "java.util.Objects.equals(" + selector + ", " + e + ")")
                .collect(Collectors.joining(" || ")));
    }

    private CtStatement getIfFromArrowCases(CtExpression<?> selector) {
        final var ifStatements = new ArrayList<CtIf>();

        swe.getCases().stream()
                .filter(swc -> !swc.getCaseExpressions().isEmpty())
                .forEach(swc -> {
                    final var ifStatement = factory.createIf();
                    ifStatement.setCondition(getIfArrowCaseExpression(selector, swc));
                    replaceYieldToAssignment(swc.getStatements(), factory);
                    ifStatement.setThenStatement(swc.getStatement(0));
                    ifStatements.add(ifStatement);
                });

        for (int ifStatementIndex = 0; ifStatementIndex < ifStatements.size() - 1; ifStatementIndex++) {
            ifStatements.get(ifStatementIndex).setElseStatement(ifStatements.get(ifStatementIndex + 1));
        }

        final var defaultCase = swe.getCases().stream()
                .filter(swc -> swc.getCaseExpressions().isEmpty())
                .findAny()
                .orElse(null);

        if (defaultCase != null) {
            replaceYieldToAssignment(defaultCase.getStatements(), factory);
            final var defaultBlock = defaultCase.getStatement(0);
            if (ifStatements.isEmpty()) {
                return defaultBlock;
            } else {
                ifStatements.get(ifStatements.size() - 1).setElseStatement(defaultBlock);
            }
        }

        if (!ifStatements.isEmpty()) {
            return ifStatements.get(0);
        }

        return factory.createCodeSnippetStatement();
    }

    public void replaceToIf() {
        if (swe.getParent() instanceof CtLambda<?>) {
            // на этом наши полномочия пока всё.
            return;
        }

        final var insertBeforeStatement = swe.getParent(CtStatement.class);

        insertBeforeStatement.insertBefore(factory.createLocalVariable(swe.getType(), switchResult, null));
        insertBeforeStatement.insertBefore(factory.createCodeSnippetStatement("final var %s = %s".formatted(
                switchValue,
                swe.getSelector()
        )));

        insertBeforeStatement.insertBefore(
                getIfFromArrowCases(swe.getFactory().createCodeSnippetExpression(switchValue))
        );

        swe.replace(factory.createCodeSnippetExpression(switchResult));
    }

    private void replaceYieldToAssignment(List<CtStatement> statements, Factory factory) {
        for (var statement : statements) {
            if (statement instanceof final CtYieldStatement yieldStatement) {
                if (statements.size() == 1) { // потому что бесит, когда if без скобосов
                    final var block = factory.createBlock();
                    block.addStatement(factory.createCodeSnippetStatement(switchResult + " = " + yieldStatement.getExpression()));
                    statement.replace(block);
                } else {
                    statement.replace(factory.createCodeSnippetStatement(switchResult + " = " + yieldStatement.getExpression()));
                }
                return;
            } else if (statement instanceof final CtBlock<?> ctBlock) {
                replaceYieldToAssignment(ctBlock.getStatements(), factory);
            }
        }
    }
}

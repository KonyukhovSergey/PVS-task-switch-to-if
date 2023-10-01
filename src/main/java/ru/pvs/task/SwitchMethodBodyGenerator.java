package ru.pvs.task;

import spoon.reflect.code.*;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SwitchMethodBodyGenerator {
    public final String methodName;

    private static final String SWITCH_VALUE = "switchValue1000";
    private static final String SWITCH_RESULT = "switchResult1000";

    private final CtSwitchExpression<?, ?> swe;

    public SwitchMethodBodyGenerator(int switchIndex, CtSwitchExpression<?, ?> swe) {
        this.methodName = "switchExpression%s".formatted(switchIndex);
        this.swe = swe;
    }

    public CtMethod<?> generate() {
        final var factory = swe.getFactory();
        final var method = factory.createMethod();
        method.setSimpleName(methodName);
        method.setType(swe.getType());

        final var parameter = factory.createParameter();
        parameter.setType(swe.getSelector().getType());
        parameter.setSimpleName(SWITCH_VALUE);

        method.addParameter(parameter);

        final var methodBody = factory.createBlock();

        methodBody.addStatement(factory.createLocalVariable(swe.getType(), SWITCH_RESULT, null));

        final var ifStatements = new ArrayList<CtIf>();

        swe.getCases().stream()
                .filter(swc -> !swc.getCaseExpressions().isEmpty())
                .forEach(swc -> {
                    final var ifStatement = factory.createIf();
                    ifStatement.setCondition(factory.createCodeSnippetExpression(swc.getCaseExpressions().stream()
                            .map(e -> "java.util.Objects.equals(" + swe.getSelector() + ", " + e.toString() + ")")
                            .collect(Collectors.joining(" || "))
                    ));
                    replaceYieldToAssignment(swc.getStatements(), factory);
                    ifStatement.setThenStatement(swc.getStatement(0));
                    ifStatements.add(ifStatement);
                });

        for (int ifStatementIndex = 0; ifStatementIndex < ifStatements.size() - 1; ifStatementIndex++) {
            ifStatements.get(ifStatementIndex).setElseStatement(ifStatements.get(ifStatementIndex + 1));
        }

        swe.getCases().stream()
                .filter(swc -> swc.getCaseExpressions().isEmpty())
                .findAny()
                .ifPresent(swc -> {
                    if (ifStatements.isEmpty()) {
                        replaceYieldToAssignment(swc.getStatements(), factory);
                        methodBody.addStatement(factory.createCodeSnippetStatement(swc.getStatement(0).toString()));
                    } else {
                        replaceYieldToAssignment(swc.getStatements(), factory);
                        ifStatements.get(ifStatements.size() - 1).setElseStatement(swc.getStatement(0));
                    }
                });

        if (!ifStatements.isEmpty()) {
            methodBody.addStatement(ifStatements.get(0));
        }
        final var returnStatement = factory.createReturn();
        returnStatement.setReturnedExpression(factory.createCodeSnippetExpression(SWITCH_RESULT));
        methodBody.addStatement(returnStatement);

        method.setBody(methodBody);

        return method;
    }

    private void replaceYieldToAssignment(List<CtStatement> statements, Factory factory) {
        for (var statement : statements) {
            if (statement instanceof CtYieldStatement yieldStatement) {
                if (statements.size() == 1) { // потому что бесит, когда if без скобосов
                    final var block = factory.createBlock();
                    block.addStatement(factory.createCodeSnippetStatement(SWITCH_RESULT + " = " + yieldStatement.getExpression()));
                    statement.replace(block);
                } else {
                    statement.replace(factory.createCodeSnippetStatement(SWITCH_RESULT + " = " + yieldStatement.getExpression()));
                }
                return;
            } else if (statement instanceof CtBlock<?> ctBlock) {
                replaceYieldToAssignment(ctBlock.getStatements(), factory);
            }
        }
    }
}

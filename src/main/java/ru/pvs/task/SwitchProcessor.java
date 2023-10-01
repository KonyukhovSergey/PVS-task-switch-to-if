package ru.pvs.task;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.support.reflect.code.CtAssignmentImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SwitchProcessor extends AbstractProcessor {
    @Override
    public boolean isToBeProcessed(CtElement candidate) {
        return true;
        //return candidate instanceof CtSwitch<?> || candidate instanceof CtSwitchExpression<?, ?> || candidate instanceof CtClass<?>;
    }

    @Override
    public void process(CtElement ctElement) {
        if (ctElement instanceof CtSwitch<?> sw) {
            process(sw);
        } else if (ctElement instanceof CtSwitchExpression<?, ?> sw) {
            process(sw);
        } else if (ctElement instanceof CtClass<?> cl) {
            cl.setSimpleName(cl.getSimpleName() + "2");
        }
    }

    private int switchExpressionIndex = 1000;

    private void process(CtSwitchExpression<?, ?> sw) {
        switchExpressionIndex++;

        final var switchMethodBodyGenerator = new SwitchMethodBodyGenerator(switchExpressionIndex, sw);
        final var switchExpressionMethod = switchMethodBodyGenerator.generate();

        sw.getParent(CtClass.class).addMethod(switchExpressionMethod);

        sw.replace(sw.getFactory().createCodeSnippetExpression(switchMethodBodyGenerator.methodName + "(" + sw.getSelector() + ")"));
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

        // if(v == e1) {stts1}
        //      else
        // if (v == e2) {stts2}
        //      ... else ...
        // {stts3}

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
                .map(dc -> "!java.util.Objects.equals(" + sw.getSelector() + ", " + dc.getCaseExpression() + ")")
                //.map(dc -> sw.getSelector() + " != " + dc.getCaseExpression())
                .collect(Collectors.joining(" && "))
                : "java.util.Objects.equals(" + sw.getSelector() + ", " + swc.getCaseExpression() + ")"
                //: sw.getSelector() + " == " + swc.getCaseExpression()
                ;
    }

    private CtBreak findLastBreakStatement(List<CtStatement> statements) {
        int lastStatementIndex = statements.size() - 1;

        while (lastStatementIndex >= 0) {
            if (!(statements.get(lastStatementIndex) instanceof CtComment)) {
                break;
            }
            lastStatementIndex--;
        }

        if (lastStatementIndex < 0) {
            return null;
        }

        final var lastStatement = statements.get(lastStatementIndex);

        if (lastStatement instanceof CtBlock<?> ctBlock) {
            return findLastBreakStatement(ctBlock.getStatements());
        }

        return (lastStatement instanceof CtBreak ctBreak) ? ctBreak : null;
    }

    private CtBreak findLastCtBreak(CtCase<?> swc) {
        return findLastBreakStatement(swc.getStatements());
    }

    private String getCodeForStatementColonCase(CtSwitch<?> sw) {
        final var expressions = new ArrayList<String>();
        final var sb = new StringBuilder();

        sw.getCases().forEach(swc -> {
            final var caseExpression = getIfExpression(swc);

            final var ifExpression = Stream.concat(expressions.stream(), Stream.of(caseExpression))
                    .collect(Collectors.joining(") || (", "(", ")"));

            final var lastBreak = findLastCtBreak(swc);

            if (lastBreak != null) {
                expressions.clear();
                lastBreak.delete();
            } else {
                expressions.add(caseExpression);
            }

            sb
                    .append("if(")
                    .append(ifExpression)
                    .append(swc.getStatements().stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(";\n    ", ") {\n    ", ";\n  }")));


        });

        return sb.toString();

    }
}

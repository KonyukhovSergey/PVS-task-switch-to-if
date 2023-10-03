package pvs.task;

import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SwitchStatementGenerator {
    private final int selectorVariableIndex;
    private final CtSwitch<?> sw;
    private final Factory factory;

    public SwitchStatementGenerator(int selectorVariableIndex, CtSwitch<?> sw) {
        this.selectorVariableIndex = selectorVariableIndex;
        this.sw = sw;
        factory = sw.getFactory();
    }

    public CtElement generate() {
//        final var originalSource = Arrays.stream("from this:%n%s".formatted(sw.toString()).split(System.lineSeparator()))
//                .map(line -> "|" + line)
//                .collect(Collectors.joining(System.lineSeparator()));

        final var switchBlockWrapper = new SwitchStatementBlockWrapper(selectorVariableIndex, sw);

        final var block = switchBlockWrapper.generate();

        if (!sw.getCases().isEmpty()) {
            if (sw.getCases().get(0).getCaseKind() == CaseKind.ARROW) {
                block.addStatement(getIfFromArrowCases(switchBlockWrapper.getSelectorVariable()));
            } else {
                block.addStatement(getIfFromColonCases(switchBlockWrapper.getSelectorVariable()));
            }
        }

//      block.addComment(factory.createComment(originalSource, CtComment.CommentType.BLOCK));
        return block;
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

        sw.getCases().stream()
                .filter(swc -> !swc.getCaseExpressions().isEmpty())
                .forEach(swc -> {
                    final var ifStatement = factory.createIf();
                    ifStatement.setCondition(getIfArrowCaseExpression(selector, swc));
                    ifStatement.setThenStatement(CaseUtils.removeLastBreak(swc));
                    ifStatements.add(ifStatement);
                });

        for (int ifStatementIndex = 0; ifStatementIndex < ifStatements.size() - 1; ifStatementIndex++) {
            ifStatements.get(ifStatementIndex).setElseStatement(ifStatements.get(ifStatementIndex + 1));
        }

        final var defaultCase = sw.getCases().stream()
                .filter(swc -> swc.getCaseExpressions().isEmpty())
                .findAny()
                .orElse(null);

        if (defaultCase != null) {
            final var defaultBlock = CaseUtils.removeLastBreak(defaultCase);
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

    private CtExpression<Boolean> getIfColonCaseExpression(CtExpression<?> selector, CtCase<?> swc) {
        return factory.createCodeSnippetExpression(swc.getCaseExpressions().isEmpty() ?
                sw.getCases().stream()
                        .filter(dc -> !dc.getCaseExpressions().isEmpty())
                        .map(dc -> CaseUtils.getNotEqualsExpression(selector, dc.getCaseExpression()))
                        .collect(Collectors.joining(" && ")) :
                CaseUtils.getEqualsExpression(selector, swc.getCaseExpression())
        );
    }

    private CtStatement getIfFromColonCases(CtExpression<?> selector) {
        if (CaseUtils.isOnlyDefaultCase(sw)) {
            final var block = factory.createBlock();
            block.setStatements(sw.getCases().get(0).getStatements());
            return block;
        }

        final var ifStatements = new ArrayList<CtIf>();
        int lastBreakCaseIndex = 0;

        for (int caseIndex = 0; caseIndex < sw.getCases().size(); caseIndex++) {
            final var swc = sw.getCases().get(caseIndex);

            final var ifStatement = factory.createIf();
            ifStatements.add(ifStatement);

            ifStatement.setCondition(getIfColonCaseExpression(selector, swc));

            final var lastBreak = CaseUtils.findLastBreak(swc);

            if (lastBreak != null) {
                lastBreak.delete();
            }

            for (int createdIfStatementIndex = lastBreakCaseIndex; createdIfStatementIndex <= caseIndex; createdIfStatementIndex++) {
                final var createdIfStatement = ifStatements.get(createdIfStatementIndex);
                if (createdIfStatement.getThenStatement() == null) {
                    createdIfStatement.setThenStatement(factory.createBlock());
                }
                final var thenCodeBlock = (CtBlock<?>) createdIfStatement.getThenStatement();
                swc.getStatements().stream()
                        .map(CtStatement::clone)
                        .forEach(thenCodeBlock::addStatement)
                ;
            }

            if (lastBreak != null) {
                lastBreakCaseIndex = caseIndex + 1;
            }
        }

        for (int ifStatementIndex = 0; ifStatementIndex < ifStatements.size() - 1; ifStatementIndex++) {
            final var statement = ifStatementIndex == ifStatements.size() - 2 && sw.getCases().get(sw.getCases().size() - 1).getCaseExpressions().isEmpty()
                    ? ifStatements.get(ifStatementIndex + 1).getThenStatement()
                    : ifStatements.get(ifStatementIndex + 1);
            ifStatements.get(ifStatementIndex).setElseStatement(statement);
        }

        if (!ifStatements.isEmpty()) {
            return ifStatements.get(0);
        }

        return factory.createCodeSnippetStatement();
    }

}

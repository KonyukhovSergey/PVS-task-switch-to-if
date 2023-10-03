package pvs.task;

import spoon.reflect.code.*;

import java.util.List;

public class CaseUtils {
    private static CtBreak findLastBreakStatement(List<CtStatement> statements) {
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

        if (lastStatement instanceof CtBlock<?>) {
            return findLastBreakStatement(((CtBlock<?>) lastStatement).getStatements());
        }

        return (lastStatement instanceof CtBreak) ? (CtBreak) lastStatement : null;
    }

    public static CtBreak findLastBreak(CtCase<?> swc) {
        return findLastBreakStatement(swc.getStatements());
    }

    public static CtStatement removeLastBreak(CtCase<?> swc) {
        final var lastBreak = findLastBreak(swc);
        if (lastBreak != null) {
            lastBreak.delete();
        }
        return swc.getStatement(0);
    }

    public static boolean isOnlyDefaultCase(CtSwitch<?> sw) {
        return sw.getCases().stream().allMatch(c -> c.getCaseExpressions().isEmpty());
    }

    public static String getNotEqualsExpression(CtExpression<?> selector, CtExpression<?> caseExpression) {
        return caseExpression.getType().isPrimitive()
                ? selector + " != " + caseExpression
                : "!java.util.Objects.equals(" + selector + ", " + caseExpression + ")";
    }

    public static String getEqualsExpression(CtExpression<?> selector, CtExpression<?> caseExpression) {
        return caseExpression.getType().isPrimitive()
                ? selector + " == " + caseExpression
                : "java.util.Objects.equals(" + selector + ", " + caseExpression + ")";
    }

}

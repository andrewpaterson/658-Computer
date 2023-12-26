package net.assembler.sixteenhigh.semanticiser.expression.assignment;

import net.assembler.sixteenhigh.semanticiser.expression.Expression;
import net.assembler.sixteenhigh.semanticiser.expression.evaluable.AddressableVariableExpression;
import net.assembler.sixteenhigh.semanticiser.expression.evaluable.EvaluableExpression;
import net.assembler.sixteenhigh.semanticiser.expression.operator.OperatorCode;

public class AssignmentExpression
    extends Expression
{
  protected AddressableVariableExpression variable;
  protected OperatorCode operator;
  protected EvaluableExpression expression;
}


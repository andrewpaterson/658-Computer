package net.assembler.sixteenhigh.semanticiser.expression.assignment;

import net.assembler.sixteenhigh.semanticiser.expression.Expression;
import net.assembler.sixteenhigh.semanticiser.expression.evaluable.AddressableVariableExpression;
import net.assembler.sixteenhigh.semanticiser.expression.evaluable.EvaluableExpression;

public class AssignmentExpression
    extends Expression
{
  protected AddressableVariableExpression variable;
  protected EvaluableExpression expression;

  public AssignmentExpression(AddressableVariableExpression variable)
  {
    this.variable = variable;
    this.expression = null;
  }
}


package net.assembler.sixteenhigh.semanticiser.expression.evaluable;

import net.assembler.sixteenhigh.semanticiser.VariableDefinition;
import net.assembler.sixteenhigh.semanticiser.expression.assignment.AccessExpression;

public class AddressableVariableExpression
    extends EvaluableExpression
{
  protected VariableDefinition variable;
  protected AccessExpression access;  //struct.member or array[x*3]

  public AddressableVariableExpression(VariableDefinition variable)
  {
    this.variable = variable;
    this.access = null;
  }
}


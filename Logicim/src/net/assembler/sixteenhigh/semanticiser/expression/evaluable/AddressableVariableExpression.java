package net.assembler.sixteenhigh.semanticiser.expression.evaluable;

import net.assembler.sixteenhigh.semanticiser.expression.assignment.AccessExpression;
import net.assembler.sixteenhigh.semanticiser.types.TypeDefinition;

public class AddressableVariableExpression
    extends EvaluableExpression
{
  protected TypeDefinition variable;
  protected AccessExpression access;  //struct.member or array[x*3]
}


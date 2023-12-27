package net.assembler.sixteenhigh.semanticiser.expression.block;

import net.assembler.sixteenhigh.semanticiser.expression.Expression;
import net.assembler.sixteenhigh.semanticiser.expression.assignment.AssignmentExpression;
import net.assembler.sixteenhigh.semanticiser.expression.evaluable.AddressableVariableExpression;

import java.util.ArrayList;
import java.util.List;

public abstract class Block
    extends Expression
{
  public List<Expression> expressions;

  public Block()
  {
    expressions = new ArrayList<>();
  }

  public void pushAssignment(AddressableVariableExpression variable)
  {
    expressions.add(new AssignmentExpression(variable));
  }
}


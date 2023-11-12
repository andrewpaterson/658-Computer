package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.FlowExpression;

public class FlowStatement
    extends Statement
{
  protected FlowExpression flowExpression;

  public FlowStatement(Statements statements, int index, FlowExpression flowExpression)
  {
    super(statements, index);
    this.flowExpression = flowExpression;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return flowExpression.print(sixteenHighKeywords) + semicolon();
  }
}


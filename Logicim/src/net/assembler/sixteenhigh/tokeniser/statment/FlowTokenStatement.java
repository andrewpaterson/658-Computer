package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.tokeniser.statment.expression.FlowExpression;

public class FlowTokenStatement
    extends TokenStatement
{
  protected FlowExpression flowExpression;

  public FlowTokenStatement(TokenUnit statements, int index, FlowExpression flowExpression)
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


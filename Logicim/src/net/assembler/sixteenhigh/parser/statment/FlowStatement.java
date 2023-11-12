package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.FlowExpression;

public class FlowStatement
    extends Statement
{
  protected FlowExpression flowExpression;

  public FlowStatement(Code code, int index, FlowExpression flowExpression)
  {
    super(code, index);
    this.flowExpression = flowExpression;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return flowExpression.print(sixteenHighKeywords) + semicolon();
  }
}


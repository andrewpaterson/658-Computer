package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.tokeniser.statment.expression.ExpressionList;

public class PushTokenStatement
    extends TokenStatement
{
  protected ExpressionList expressions;

  public PushTokenStatement(TokenUnit statements, int index, ExpressionList expressions)
  {
    super(statements, index);
    this.expressions = expressions;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "> " + expressions.print(sixteenHighKeywords) + semicolon();
  }
}


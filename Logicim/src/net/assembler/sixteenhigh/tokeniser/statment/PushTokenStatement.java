package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.tokeniser.statment.expression.TokenExpressionList;

public class PushTokenStatement
    extends TokenStatement
{
  protected TokenExpressionList expressions;

  public PushTokenStatement(TokenUnit statements, int index, TokenExpressionList expressions)
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


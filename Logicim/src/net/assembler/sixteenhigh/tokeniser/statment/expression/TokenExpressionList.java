package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;

import java.util.ArrayList;
import java.util.List;

public class TokenExpressionList
    extends BaseTokenExpression
{
  public List<TokenExpression> expressions;

  public TokenExpressionList()
  {
    super();
    expressions = new ArrayList<>();
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    StringBuilder builder = new StringBuilder();
    if (expressions.size() > 1)
    {
      builder.append("(");
    }
    printExpressions(expressions, sixteenHighKeywords, builder);
    if (expressions.size() > 1)
    {
      builder.append(")");
    }
    return builder.toString();
  }

  @Override
  public boolean isExpression()
  {
    return true;
  }

  public void add(TokenExpression expression)
  {
    expressions.add(expression);
  }

  @Override
  public boolean isLiteral()
  {
    if (expressions.size() == 1)
    {
      return expressions.get(0).isLiteral();
    }
    return false;
  }

  @Override
  public boolean isAssignment()
  {
    return true;
  }
}

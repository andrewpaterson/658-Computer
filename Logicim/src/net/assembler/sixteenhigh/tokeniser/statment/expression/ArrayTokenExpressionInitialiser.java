package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;

import java.util.ArrayList;
import java.util.List;

public class ArrayTokenExpressionInitialiser
    extends BaseTokenExpression
{
  protected List<TokenExpression> expressions;

  public ArrayTokenExpressionInitialiser()
  {
    super();
    expressions = new ArrayList<>();
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    if (containsArrayExpressionInitialiser(expressions))
    {
      printSeparatedExpressions(expressions, sixteenHighKeywords, builder);
    }
    else
    {
      printCommaSeparatedExpressions(expressions, sixteenHighKeywords, builder);
    }
    builder.append("]");
    return builder.toString();
  }

  @Override
  public boolean isArrayInitialiser()
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


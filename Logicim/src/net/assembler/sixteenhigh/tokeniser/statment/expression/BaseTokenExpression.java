package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;

import java.util.List;

public abstract class BaseTokenExpression
    implements TokenExpression
{
  public BaseTokenExpression()
  {
  }

  protected void printCommaSeparatedExpressions(List<TokenExpression> expressions, SixteenHighKeywords sixteenHighKeywords, StringBuilder builder)
  {
    boolean isFirst = true;
    for (TokenExpression expression : expressions)
    {
      if (isFirst)
      {
        isFirst = false;
      }
      else
      {
        builder.append(", ");
      }
      builder.append(expression.print(sixteenHighKeywords));
    }
  }

  protected void printSeparatedExpressions(List<TokenExpression> expressions, SixteenHighKeywords sixteenHighKeywords, StringBuilder builder)
  {
    for (TokenExpression expression : expressions)
    {
      builder.append(expression.print(sixteenHighKeywords));
    }
  }

  protected boolean containsArrayExpressionInitialiser(List<TokenExpression> expressions)
  {
    for (TokenExpression expression : expressions)
    {
      if (expression.isArrayInitialiser())
      {
        return true;
      }
    }
    return false;
  }

  protected void printExpressions(List<TokenExpression> expressions, SixteenHighKeywords sixteenHighKeywords, StringBuilder builder)
  {
    boolean isFirst = true;
    for (TokenExpression expression : expressions)
    {
      if (isFirst)
      {
        isFirst = false;
      }
      else
      {
        builder.append(" ");
      }
      builder.append(expression.print(sixteenHighKeywords));
    }
  }

  public boolean isAssignment()
  {
    return false;
  }

  public abstract void add(TokenExpression expression);

  @Override
  public abstract boolean isLiteral();

  @Override
  public String toString()
  {
    return print(SixteenHighKeywords.getInstance());
  }
}


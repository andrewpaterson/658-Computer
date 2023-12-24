package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;

import java.util.List;

public abstract class BaseExpression
    implements Expression
{
  public BaseExpression()
  {
  }

  protected void printCommaSeparatedExpressions(List<Expression> expressions, SixteenHighKeywords sixteenHighKeywords, StringBuilder builder)
  {
    boolean isFirst = true;
    for (Expression expression : expressions)
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

  protected void printSeparatedExpressions(List<Expression> expressions, SixteenHighKeywords sixteenHighKeywords, StringBuilder builder)
  {
    for (Expression expression : expressions)
    {
      builder.append(expression.print(sixteenHighKeywords));
    }
  }

  protected boolean containsArrayExpressionInitialiser(List<Expression> expressions)
  {
    for (Expression expression : expressions)
    {
      if (expression.isArrayExpressionInitialiser())
      {
        return true;
      }
    }
    return false;
  }

  protected void printExpressions(List<Expression> expressions, SixteenHighKeywords sixteenHighKeywords, StringBuilder builder)
  {
    boolean isFirst = true;
    for (Expression expression : expressions)
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

  public abstract void add(Expression expression);

  @Override
  public abstract boolean isLiteral();

  public abstract boolean isAssignment();
}


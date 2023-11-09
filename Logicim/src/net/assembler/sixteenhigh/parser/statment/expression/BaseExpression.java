package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseExpression
    implements Expressable
{
  public List<Expressable> expressions;

  public BaseExpression()
  {
    expressions = new ArrayList<>();
  }

  protected void printCommaSeparatedExpressions(SixteenHighKeywords sixteenHighKeywords, StringBuilder builder)
  {
    boolean isFirst = true;
    for (Expressable expressable : expressions)
    {
      if (isFirst)
      {
        isFirst = false;
      }
      else
      {
        builder.append(", ");
      }
      builder.append(expressable.print(sixteenHighKeywords));
    }
  }

  protected void printSeparatedExpressions(SixteenHighKeywords sixteenHighKeywords, StringBuilder builder)
  {
    for (Expressable expressable : expressions)
    {
      builder.append(expressable.print(sixteenHighKeywords));
    }
  }

  protected boolean containsArrayExpressionInitialiser()
  {
    for (Expressable expressable : expressions)
    {
      if (expressable.isArrayExpressionInitialiser())
      {
        return true;
      }
    }
    return false;
  }

  protected void printExpressions(SixteenHighKeywords sixteenHighKeywords, StringBuilder builder)
  {
    boolean isFirst = true;
    for (Expressable expressable : expressions)
    {
      if (isFirst)
      {
        isFirst = false;
      }
      else
      {
        builder.append(" ");
      }
      builder.append(expressable.print(sixteenHighKeywords));
    }
  }

  public void add(Expressable expressable)
  {
    expressions.add(expressable);
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
}


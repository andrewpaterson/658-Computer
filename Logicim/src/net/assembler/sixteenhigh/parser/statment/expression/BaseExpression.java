package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

import java.util.List;

public abstract class BaseExpression
    implements Expressable
{
  public BaseExpression()
  {
  }

  protected void printCommaSeparatedExpressions(List<Expressable> expressions, SixteenHighKeywords sixteenHighKeywords, StringBuilder builder)
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

  protected void printSeparatedExpressions(List<Expressable> expressions, SixteenHighKeywords sixteenHighKeywords, StringBuilder builder)
  {
    for (Expressable expressable : expressions)
    {
      builder.append(expressable.print(sixteenHighKeywords));
    }
  }

  protected boolean containsArrayExpressionInitialiser(List<Expressable> expressions)
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

  protected void printExpressions(List<Expressable> expressions, SixteenHighKeywords sixteenHighKeywords, StringBuilder builder)
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

  public abstract void add(Expressable expressable);

  @Override
  public abstract boolean isLiteral();

  public abstract boolean isAssignment();
}


package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

import java.util.ArrayList;
import java.util.List;

public class Expression
    implements Expressable
{
  public List<Expressable> expressables;

  public Expression()
  {
    expressables = new ArrayList<>();
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    boolean expression = false;
    for (Expressable expressable : expressables)
    {
      if (expressable.isExpression())
      {
        expression = true;
        break;
      }
    }


    StringBuilder builder = new StringBuilder();
    if (expression)
    {
      builder.append("(");
    }
    boolean isFirst = true;
    for (Expressable expressable : expressables)
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
    if (expression)
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

  public void add(Expressable expressable)
  {
    expressables.add(expressable);
  }
}


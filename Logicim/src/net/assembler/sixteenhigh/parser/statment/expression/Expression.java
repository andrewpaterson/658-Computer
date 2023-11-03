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
    StringBuilder builder = new StringBuilder();
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
    return builder.toString();
  }

  public void add(Expressable expressable)
  {
    expressables.add(expressable);
  }
}


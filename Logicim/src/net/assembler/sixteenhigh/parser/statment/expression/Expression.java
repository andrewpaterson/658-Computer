package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

import java.util.ArrayList;
import java.util.List;

public class Expression
    extends BaseExpression
{
  public List<Expressable> expressions;

  public Expression()
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

  @Override
  public boolean isAssignment()
  {
    return true;
  }
}


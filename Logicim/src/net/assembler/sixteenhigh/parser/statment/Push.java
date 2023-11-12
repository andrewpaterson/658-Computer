package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.Expression;

public class Push
    extends Statement
{
  protected Expression expression;

  public Push(Statements statements, int index, Expression expression)
  {
    super(statements, index);
    this.expression = expression;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "> " + expression.print(sixteenHighKeywords) + semicolon();
  }
}


package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.tokeniser.statment.expression.Expression;

public class PushStatement
    extends Statement
{
  protected Expression expression;

  public PushStatement(Statements statements, int index, Expression expression)
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


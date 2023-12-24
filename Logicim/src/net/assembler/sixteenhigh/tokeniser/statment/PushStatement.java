package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.tokeniser.statment.expression.ExpressionList;

public class PushStatement
    extends Statement
{
  protected ExpressionList expressions;

  public PushStatement(Statements statements, int index, ExpressionList expressions)
  {
    super(statements, index);
    this.expressions = expressions;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "> " + expressions.print(sixteenHighKeywords) + semicolon();
  }
}


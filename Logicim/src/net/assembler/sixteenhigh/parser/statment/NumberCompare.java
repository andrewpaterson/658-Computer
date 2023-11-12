package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.Expression;
import net.assembler.sixteenhigh.parser.statment.expression.VariableExpression;

public class NumberCompare
    extends Statement
{
  protected VariableExpression leftVariableExpression;
  protected Expression rightExpression;
  protected SixteenHighKeywordCode keyword;

  public NumberCompare(Statements statements,
                       int index,
                       VariableExpression leftVariableExpression,
                       Expression rightExpression,
                       SixteenHighKeywordCode keyword)
  {
    super(statements, index);
    this.leftVariableExpression = leftVariableExpression;
    this.rightExpression = rightExpression;
    this.keyword = keyword;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return leftVariableExpression.print(sixteenHighKeywords) + " " + sixteenHighKeywords.getKeyword(keyword) + " " + rightExpression.print(sixteenHighKeywords) + semicolon();
  }
}


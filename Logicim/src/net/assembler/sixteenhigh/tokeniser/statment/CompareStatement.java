package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.expression.Expression;
import net.assembler.sixteenhigh.tokeniser.statment.expression.VariableExpression;

public class CompareStatement
    extends Statement
{
  protected VariableExpression leftVariableExpression;
  protected Expression rightExpression;
  protected SixteenHighKeywordCode keyword;

  public CompareStatement(Statements statements,
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


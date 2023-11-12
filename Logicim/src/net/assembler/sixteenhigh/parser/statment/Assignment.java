package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.Expression;
import net.assembler.sixteenhigh.parser.statment.expression.VariableExpression;

public class Assignment
    extends Statement
{
  protected VariableExpression leftVariableExpression;
  protected SixteenHighKeywordCode keyword;
  protected Expression rightExpression;

  public Assignment(Statements statements,
                    int index,
                    VariableExpression leftVariableExpression,
                    SixteenHighKeywordCode keyword,
                    Expression rightExpression)
  {
    super(statements, index);
    this.leftVariableExpression = leftVariableExpression;
    this.keyword = keyword;
    this.rightExpression = rightExpression;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return leftVariableExpression.print(sixteenHighKeywords) + " " + sixteenHighKeywords.getKeyword(keyword) + " " + rightExpression.print(sixteenHighKeywords) + semicolon();
  }
}


package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.expression.Expression;
import net.assembler.sixteenhigh.tokeniser.statment.expression.VariableExpression;

public class AssignmentStatement
    extends Statement
{
  protected VariableExpression leftVariableExpression;
  protected SixteenHighKeywordCode keyword;
  protected Expression rightExpression;

  public AssignmentStatement(Statements statements,
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


package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.expression.ExpressionList;
import net.assembler.sixteenhigh.tokeniser.statment.expression.VariableExpression;

public class AssignmentTokenStatement
    extends TokenStatement
{
  protected VariableExpression leftVariableExpression;
  protected SixteenHighKeywordCode keyword;
  protected ExpressionList rightExpressions;

  public AssignmentTokenStatement(TokenUnit statements,
                                  int index,
                                  VariableExpression leftVariableExpression,
                                  SixteenHighKeywordCode keyword,
                                  ExpressionList rightExpressions)
  {
    super(statements, index);
    this.leftVariableExpression = leftVariableExpression;
    this.keyword = keyword;
    this.rightExpressions = rightExpressions;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return leftVariableExpression.print(sixteenHighKeywords) + " " + sixteenHighKeywords.getKeyword(keyword) + " " + rightExpressions.print(sixteenHighKeywords) + semicolon();
  }
}


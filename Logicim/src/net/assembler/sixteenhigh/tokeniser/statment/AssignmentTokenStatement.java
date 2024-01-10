package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.expression.TokenExpressionList;
import net.assembler.sixteenhigh.tokeniser.statment.expression.VariableTokenExpression;

public class AssignmentTokenStatement
    extends TokenStatement
{
  protected VariableTokenExpression leftVariableExpression;
  protected SixteenHighKeywordCode keyword;
  protected TokenExpressionList rightExpressions;

  public AssignmentTokenStatement(TokenUnit statements,
                                  int index,
                                  VariableTokenExpression leftVariableExpression,
                                  SixteenHighKeywordCode keyword,
                                  TokenExpressionList rightExpressions)
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

  @Override
  public boolean isAssignment()
  {
    return true;
  }

  public VariableTokenExpression getLeftVariableExpression()
  {
    return leftVariableExpression;
  }

  public SixteenHighKeywordCode getKeyword()
  {
    return keyword;
  }

  public TokenExpressionList getRightExpressions()
  {
    return rightExpressions;
  }
}


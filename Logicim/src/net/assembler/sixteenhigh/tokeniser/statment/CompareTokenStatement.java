
package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.expression.TokenExpressionList;
import net.assembler.sixteenhigh.tokeniser.statment.expression.VariableTokenExpression;

public class CompareTokenStatement
    extends TokenStatement
{
  protected VariableTokenExpression leftVariableExpression;
  protected TokenExpressionList rightExpressions;
  protected SixteenHighKeywordCode keyword;

  public CompareTokenStatement(TokenUnit statements,
                               int index,
                               VariableTokenExpression leftVariableExpression,
                               TokenExpressionList rightExpressions,
                               SixteenHighKeywordCode keyword)
  {
    super(statements, index);
    this.leftVariableExpression = leftVariableExpression;
    this.rightExpressions = rightExpressions;
    this.keyword = keyword;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return leftVariableExpression.print(sixteenHighKeywords) + " " + sixteenHighKeywords.getKeyword(keyword) + " " + rightExpressions.print(sixteenHighKeywords) + semicolon();
  }
}


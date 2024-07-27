<<<<<<< Updated upstream
package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.expression.TokenExpressionList;
import net.assembler.sixteenhigh.tokeniser.statment.expression.VariableTokenExpression;

public class CompareTokenStatement
    extends TokenStatement
{
  protected VariableTokenExpression leftVariableExpression;
  protected TokenExpressionList rightExpressions;
  protected SixteenHighKeywordCode keyword;

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return leftVariableExpression.print(sixteenHighKeywords) + " " + sixteenHighKeywords.getKeyword(keyword) + " " + rightExpressions.print(sixteenHighKeywords) + semicolon();
  }
}

=======
package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.expression.TokenExpressionList;
import net.assembler.sixteenhigh.tokeniser.statment.expression.VariableTokenExpression;

public class CompareTokenStatement
    extends TokenStatement
{
  protected VariableTokenExpression leftVariableExpression;
  protected TokenExpressionList rightExpressions;
  protected SixteenHighKeywordCode keyword;

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return leftVariableExpression.print(sixteenHighKeywords) + " " + sixteenHighKeywords.getKeyword(keyword) + " " + rightExpressions.print(sixteenHighKeywords) + semicolon();
  }
}

>>>>>>> Stashed changes

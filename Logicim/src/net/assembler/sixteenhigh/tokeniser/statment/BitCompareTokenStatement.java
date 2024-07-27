
package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.expression.VariableTokenExpression;

public class BitCompareTokenStatement
    extends TokenStatement
{
  protected VariableTokenExpression variableExpression;
  protected SixteenHighKeywordCode keyword;

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return variableExpression.print(sixteenHighKeywords) + " " + sixteenHighKeywords.getKeyword(keyword) + semicolon();
  }
}

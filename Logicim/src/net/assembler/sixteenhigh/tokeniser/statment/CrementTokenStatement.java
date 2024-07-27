package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.expression.VariableTokenExpression;

public class CrementTokenStatement
    extends TokenStatement
{
  protected VariableTokenExpression variableExpression;
  protected SixteenHighKeywordCode keyword;

  public CrementTokenStatement(TokenUnit statements,
                               int index,
                               VariableTokenExpression variableExpression,
                               SixteenHighKeywordCode keyword)
  {
    super(statements, index);
    this.variableExpression = variableExpression;
    this.keyword = keyword;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return variableExpression.print(sixteenHighKeywords) + sixteenHighKeywords.getKeyword(keyword) + semicolon();
  }
}


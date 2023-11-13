package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.expression.VariableExpression;

public class Crement
    extends Statement
{
  protected VariableExpression variableExpression;
  protected SixteenHighKeywordCode keyword;

  public Crement(Statements statements,
                 int index,
                 VariableExpression variableExpression,
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


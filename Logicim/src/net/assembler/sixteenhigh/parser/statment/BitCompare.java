package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.VariableExpression;

public class BitCompare
    extends Statement
{
  protected VariableExpression variableExpression;
  protected SixteenHighKeywordCode keyword;

  public BitCompare(Statements statements,
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
    return variableExpression.print(sixteenHighKeywords) + " " + sixteenHighKeywords.getKeyword(keyword) + semicolon();
  }
}


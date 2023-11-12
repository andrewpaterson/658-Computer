package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.RegisterExpression;

public class BitCompare
    extends Statement
{
  protected RegisterExpression register;
  protected SixteenHighKeywordCode keyword;

  public BitCompare(Statements statements,
                    int index,
                    RegisterExpression register,
                    SixteenHighKeywordCode keyword)
  {
    super(statements, index);
    this.register = register;
    this.keyword = keyword;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return register.print(sixteenHighKeywords) + " " + sixteenHighKeywords.getKeyword(keyword) + semicolon();
  }
}


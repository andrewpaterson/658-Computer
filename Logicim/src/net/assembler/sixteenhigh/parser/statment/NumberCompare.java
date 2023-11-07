package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.Expression;
import net.assembler.sixteenhigh.parser.statment.expression.RegisterExpression;

public class NumberCompare
    extends Statement
{
  protected RegisterExpression leftRegister;
  protected Expression right;
  protected SixteenHighKeywordCode keyword;

  public NumberCompare(Code code,
                       int index,
                       RegisterExpression leftRegister,
                       Expression right,
                       SixteenHighKeywordCode keyword)
  {
    super(code, index);
    this.leftRegister = leftRegister;
    this.right = right;
    this.keyword = keyword;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return leftRegister.print(sixteenHighKeywords) + " " + sixteenHighKeywords.getKeyword(keyword) + " " + right.print(sixteenHighKeywords) + semicolon();
  }
}


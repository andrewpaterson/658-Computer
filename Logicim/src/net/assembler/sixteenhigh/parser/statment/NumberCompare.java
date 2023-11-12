package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Statements;
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

  public NumberCompare(Statements statements,
                       int index,
                       RegisterExpression leftRegister,
                       Expression right,
                       SixteenHighKeywordCode keyword)
  {
    super(statements, index);
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


package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.RegisterExpression;

public class Pull
    extends Statement
{
  protected RegisterExpression register;

  public Pull(Code code, int index, RegisterExpression register)
  {
    super(code, index);
    this.register = register;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return register.print(sixteenHighKeywords) + "<" + semicolon();
  }
}


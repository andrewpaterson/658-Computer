package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;

public class Ret
    extends Statement
{
  public Ret(Statements statements, int index)
  {
    super(statements, index);
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "return" + semicolon();
  }
}


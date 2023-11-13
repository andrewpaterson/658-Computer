package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.common.SixteenHighKeywords;

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


package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public class Gosub
    extends Statement
{
  protected String label;

  public Gosub(Code code, int index, String label)
  {
    super(code, index);
    this.label = label;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "gosub " + label + semicolon();
  }
}


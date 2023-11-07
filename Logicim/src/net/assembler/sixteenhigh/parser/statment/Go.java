package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public class Go
    extends Statement
{
  protected String label;

  public Go(Code code, int index, String label)
  {
    super(code, index);
    this.label = label;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "go " + label + semicolon();
  }
}


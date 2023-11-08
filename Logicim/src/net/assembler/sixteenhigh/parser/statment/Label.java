package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public class Label
    extends Statement
{
  protected String name;

  public Label(Code code, int index, String name)
  {
    super(code, index);
    this.name = name;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return name + ":";
  }

  @Override
  public boolean isLabel()
  {
    return true;
  }
}


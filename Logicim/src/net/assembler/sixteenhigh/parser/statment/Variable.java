package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;

public class Variable
    extends Statement
{
  public SixteenHighKeywordCode type;
  public String name;

  public Variable(Code code, int index, SixteenHighKeywordCode type, String name)
  {
    super(code, index);
    this.type = type;
    this.name = name;
  }
}


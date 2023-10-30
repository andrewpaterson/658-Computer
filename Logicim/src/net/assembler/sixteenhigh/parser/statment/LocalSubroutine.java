package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public class LocalSubroutine
    extends Routine
{
  public LocalSubroutine(Code code, int index, String name)
  {
    super(code, index, name);
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return name;
  }
}


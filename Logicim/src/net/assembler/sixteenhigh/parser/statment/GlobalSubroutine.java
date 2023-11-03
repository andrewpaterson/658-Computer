package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public class GlobalSubroutine
    extends Routine
{
  public GlobalSubroutine(Code code, int index, String name)
  {
    super(code, index, name);
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return name;
  }
}


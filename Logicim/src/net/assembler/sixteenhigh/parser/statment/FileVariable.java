package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;

public class FileVariable
    extends Variable
{
  public FileVariable(Code code, int index, SixteenHighKeywordCode type, String name)
  {
    super(code, index, type, name);
  }
}

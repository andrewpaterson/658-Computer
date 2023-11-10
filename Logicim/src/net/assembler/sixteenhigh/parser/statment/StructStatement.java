package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.types.Struct;

public class StructStatement
    extends Statement
{
  public Struct struct;

  public StructStatement(Code code, int index, Struct struct)
  {
    super(code, index);
    this.struct = struct;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return null;
  }
}


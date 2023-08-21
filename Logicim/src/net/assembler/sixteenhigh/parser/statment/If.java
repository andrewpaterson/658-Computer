package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;

public class If
    extends Statement
{
  protected SixteenHighKeywordCode type;
  protected Go go;

  public If(Code code, int index, SixteenHighKeywordCode type)
  {
    super(code, index);
    this.type = type;
  }

  public void setGo(Go go)
  {
    this.go = go;
  }
}


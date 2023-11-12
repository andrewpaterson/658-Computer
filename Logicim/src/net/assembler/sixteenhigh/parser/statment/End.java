package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public class End
    extends Statement
{
  public End(Statements statements, int index)
  {
    super(statements, index);
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "end\n";
  }

  public boolean isEnd()
  {
    return true;
  }
}


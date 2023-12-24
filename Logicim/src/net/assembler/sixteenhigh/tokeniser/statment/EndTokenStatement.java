package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;

public class EndTokenStatement
    extends TokenStatement
{
  public EndTokenStatement(TokenUnit statements, int index)
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


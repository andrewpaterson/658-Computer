package net.assembler.sixteenhigh.parser.statment.directive;

import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.Statement;

public class AccessMode
    extends Statement
{
  protected SixteenHighKeywordCode mode;

  public AccessMode(Statements statements, int index, SixteenHighKeywordCode mode)
  {
    super(statements, index);
    this.mode = mode;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "$access_mode " + sixteenHighKeywords.getKeyword(mode);
  }
}


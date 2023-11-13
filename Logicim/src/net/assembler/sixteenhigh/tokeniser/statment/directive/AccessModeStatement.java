package net.assembler.sixteenhigh.tokeniser.statment.directive;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;

public class AccessModeStatement
    extends DirectiveStatement
{
  protected SixteenHighKeywordCode mode;

  public AccessModeStatement(Statements statements, int index, SixteenHighKeywordCode mode)
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


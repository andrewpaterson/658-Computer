package net.assembler.sixteenhigh.parser.statment.directive;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.statment.Statement;

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


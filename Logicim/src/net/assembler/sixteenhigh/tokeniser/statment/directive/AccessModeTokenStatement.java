package net.assembler.sixteenhigh.tokeniser.statment.directive;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.semanticiser.directive.AccessMode;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;

public class AccessModeTokenStatement
    extends DirectiveTokenStatement
{
  protected SixteenHighKeywordCode mode;

  public AccessModeTokenStatement(TokenUnit statements, int index, SixteenHighKeywordCode mode)
  {
    super(statements, index);
    this.mode = mode;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "$access_mode " + sixteenHighKeywords.getKeyword(mode);
  }

  public AccessMode getAccessMode()
  {
    if (mode == SixteenHighKeywordCode.write_only)
    {
      return AccessMode.WRITE_ONLY;
    }
    if (mode == SixteenHighKeywordCode.read_only)
    {
      return AccessMode.READ_ONLY;
    }
    if (mode == SixteenHighKeywordCode.read_write)
    {
      return AccessMode.READ_WRITE;
    }

    return null;
  }
}


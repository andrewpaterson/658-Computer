package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public class AssignmentFromIdentifier
    extends Statement
{
  protected String leftRegister;
  protected String rightRegister;
  protected SixteenHighKeywordCode keyword;

  public AssignmentFromIdentifier(Code code,
                                  int index,
                                  String leftRegister,
                                  String rightRegister,
                                  SixteenHighKeywordCode keyword)
  {
    super(code, index);
    this.leftRegister = leftRegister;
    this.rightRegister = rightRegister;
    this.keyword = keyword;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return leftRegister + " " + sixteenHighKeywords.getKeyword(keyword) + " " + rightRegister;
  }
}


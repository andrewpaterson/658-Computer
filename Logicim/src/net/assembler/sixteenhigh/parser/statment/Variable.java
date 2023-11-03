package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.common.util.StringUtil;

public class Variable
    extends Statement
{
  public SixteenHighKeywordCode type;
  public String name;
  public int asteriskCount;

  public Variable(Code code,
                  int index,
                  SixteenHighKeywordCode type,
                  String name,
                  int asteriskCount)
  {
    super(code, index);
    this.type = type;
    this.name = name;
    this.asteriskCount = asteriskCount;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    StringBuilder asterisks = StringUtil.pad("*", asteriskCount);
    return sixteenHighKeywords.getKeyword(type) + asterisks.toString() + " " + name;
  }
}


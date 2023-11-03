package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.common.util.StringUtil;

import java.util.List;

public class Variable
    extends Statement
{
  public SixteenHighKeywordCode type;
  public String name;
  public List<Long> arrayMatrix;
  public int asteriskCount;

  public Variable(Code code,
                  int index,
                  SixteenHighKeywordCode type,
                  String name,
                  List<Long> arrayMatrix,
                  int asteriskCount)
  {
    super(code, index);
    this.type = type;
    this.name = name;
    this.arrayMatrix = arrayMatrix;
    this.asteriskCount = asteriskCount;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    StringBuilder arrays = new StringBuilder();
    for (Long arrayLength : arrayMatrix)
    {
      arrays.append("[");
      arrays.append(arrayLength.longValue());
      arrays.append("]");
    }
    StringBuilder asterisks = StringUtil.pad("*", asteriskCount);
    return sixteenHighKeywords.getKeyword(type) + arrays.toString() + asterisks.toString() + " " + name;
  }
}


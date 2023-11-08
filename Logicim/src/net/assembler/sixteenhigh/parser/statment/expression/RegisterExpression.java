package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class RegisterExpression
    implements Expressable
{
  public int dereferenceCount;
  public boolean reference;
  public String registerName;
  public List<Expressable> arrayIndices;

  public RegisterExpression(String registerName, int dereferenceCount, boolean reference)
  {
    this.registerName = registerName;
    this.dereferenceCount = dereferenceCount;
    this.reference = reference;
    this.arrayIndices = new ArrayList<>();
  }

  public void addArrayIndex(Expressable expressable)
  {
    arrayIndices.add(expressable);
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    String reference = this.reference ? "&" : "";
    StringBuilder asterisks = StringUtil.pad("*", dereferenceCount);
    StringBuilder array = new StringBuilder();
    for (Expressable arrayIndex : arrayIndices)
    {
      array.append("[");
      array.append(arrayIndex.print(sixteenHighKeywords));
      array.append("]");
    }
    return asterisks + reference + registerName + array.toString();
  }
}


package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;

import java.util.ArrayList;
import java.util.List;

public class VariableMember
{
  public String identifier;
  public List<Expressable> arrayIndices;

  public VariableMember(String identifier)
  {
    this.identifier = identifier;
    this.arrayIndices = new ArrayList<>();
  }

  public void addArrayIndex(Expressable expressable)
  {
    arrayIndices.add(expressable);
  }

  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    StringBuilder array = new StringBuilder();
    for (Expressable arrayIndex : arrayIndices)
    {
      array.append("[");
      array.append(arrayIndex.print(sixteenHighKeywords));
      array.append("]");
    }
    return identifier + array.toString();
  }
}


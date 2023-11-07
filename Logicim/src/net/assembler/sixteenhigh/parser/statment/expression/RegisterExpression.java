package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

import java.util.ArrayList;
import java.util.List;

public class RegisterExpression
    implements Expressable
{
  public String registerName;
  public List<Expressable> arrayIndices;

  public RegisterExpression(String registerName)
  {
    this.registerName = registerName;
    this.arrayIndices = new ArrayList<>();
  }

  public void addArrayIndex(Expressable expressable)
  {
    arrayIndices.add(expressable);
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    StringBuilder array = new StringBuilder();
    for (Expressable arrayIndex : arrayIndices)
    {
      array.append("[");
      array.append(arrayIndex.print(sixteenHighKeywords));
      array.append("]");
    }
    return registerName + array.toString();
  }
}


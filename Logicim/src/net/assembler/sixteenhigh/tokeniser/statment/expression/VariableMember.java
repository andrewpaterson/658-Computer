package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;

import java.util.ArrayList;
import java.util.List;

public class VariableMember
{
  public String identifier;
  public List<Expression> arrayIndices;

  public VariableMember(String identifier)
  {
    this.identifier = identifier;
    this.arrayIndices = new ArrayList<>();
  }

  public void addArrayIndex(Expression expression)
  {
    arrayIndices.add(expression);
  }

  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    StringBuilder array = new StringBuilder();
    for (Expression arrayIndex : arrayIndices)
    {
      array.append("[");
      array.append(arrayIndex.print(sixteenHighKeywords));
      array.append("]");
    }
    return identifier + array.toString();
  }
}


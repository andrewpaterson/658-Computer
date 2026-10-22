package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;

import java.util.ArrayList;
import java.util.List;

public class VariableMember
{
  protected String identifier;
  protected List<TokenExpression> arrayIndices;

  public VariableMember(String identifier)
  {
    this.identifier = identifier;
    this.arrayIndices = new ArrayList<>();
  }

  public void addArrayIndex(TokenExpression expression)
  {
    arrayIndices.add(expression);
  }

  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    StringBuilder array = new StringBuilder();
    for (TokenExpression arrayIndex : arrayIndices)
    {
      array.append("[");
      array.append(arrayIndex.print(sixteenHighKeywords));
      array.append("]");
    }
    return identifier + array.toString();
  }

  public String getIdentifier()
  {
    return identifier;
  }

  public List<TokenExpression> getArrayIndices()
  {
    return arrayIndices;
  }
}


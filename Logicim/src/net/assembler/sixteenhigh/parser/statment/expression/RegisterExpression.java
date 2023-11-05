package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public class RegisterExpression
    implements Expressable
{
  public String registerName;

  public RegisterExpression(String registerName)
  {
    this.registerName = registerName;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return registerName;
  }
}


package net.assembler.sixteenhigh.parser.literal;

public class CTBoolean
    extends CTLiteral
{
  private boolean value;

  public CTBoolean(boolean value)
  {
    this.value = value;
  }

  public boolean isValue()
  {
    return value;
  }
}

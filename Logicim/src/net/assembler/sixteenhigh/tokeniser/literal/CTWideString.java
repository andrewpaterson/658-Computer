package net.assembler.sixteenhigh.tokeniser.literal;

public class CTWideString
    extends CTLiteral
{
  protected String value;

  public CTWideString(String value)
  {
    this.value = value;
  }

  @Override
  public String print()
  {
    return "\"" + value + "\"";
  }
}

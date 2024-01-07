package net.assembler.sixteenhigh.tokeniser.literal;

public class CTString
    extends CTLiteral
{
  protected String value;

  public CTString(String value)
  {
    this.value = value;
  }

  @Override
  public String print()
  {
    return "\"" + value + "\"";
  }

  public String getValue()
  {
    return value;
  }
}


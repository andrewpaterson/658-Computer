package net.assembler.sixteenhigh.parser.literal;

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
}


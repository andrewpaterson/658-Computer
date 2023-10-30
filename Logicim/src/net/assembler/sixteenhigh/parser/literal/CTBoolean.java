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

  public boolean getValue()
  {
    return value;
  }

  @Override
  public String print()
  {
    if (value)
    {
      return "true";
    }
    else
    {
      return "false";
    }
  }
}


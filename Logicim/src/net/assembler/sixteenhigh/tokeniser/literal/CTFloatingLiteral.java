package net.assembler.sixteenhigh.tokeniser.literal;

public abstract class CTFloatingLiteral
    extends CTLiteral
{
  public abstract double getValue();

  public abstract boolean isValid();

  @Override
  public String print()
  {
    return Double.toString(getValue());
  }
}


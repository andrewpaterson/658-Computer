package net.assembler.sixteenhigh.parser.literal;

public class CTLongDouble
    extends CTFloatingLiteral
{
  public double value;

  public CTLongDouble(double value)
  {
    this.value = value;
  }

  @Override
  public double getValue()
  {
    return value;
  }

  @Override
  public boolean isValid()
  {
    return true;
  }
}


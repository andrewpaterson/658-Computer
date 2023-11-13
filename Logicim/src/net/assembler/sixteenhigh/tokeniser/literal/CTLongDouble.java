package net.assembler.sixteenhigh.tokeniser.literal;

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

  @Override
  public String print()
  {
    return super.print() + "L";
  }
}


package net.assembler.sixteenhigh.parser.literal;

public class CTFloat
    extends CTFloatingLiteral
{
  public double value;

  public CTFloat(double value)
  {
    this.value = value;
  }

  public double getValue()
  {
    return value;
  }

  @Override
  public boolean isValid()
  {
    if ((value > Float.MAX_VALUE) || (value < -Float.MAX_VALUE))
    {
      return false;
    }
    if ((Math.abs(value) < Float.MIN_NORMAL))
    {
      return false;
    }
    return true;
  }
}


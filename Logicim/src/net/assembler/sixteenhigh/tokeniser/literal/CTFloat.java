package net.assembler.sixteenhigh.tokeniser.literal;

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
    return true;
  }

  @Override
  public String print()
  {
    return super.print() + "F";
  }
}


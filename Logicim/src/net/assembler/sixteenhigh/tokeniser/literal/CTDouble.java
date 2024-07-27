package net.assembler.sixteenhigh.tokeniser.literal;

public class CTDouble
    extends CTFloatingLiteral
{
  public double value;

  public CTDouble(double value)
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
    return true;
  }

  @Override
  public PrimitiveTypeCode getPrimitiveTypeCode()
  {
    return PrimitiveTypeCode.float64;
  }
}


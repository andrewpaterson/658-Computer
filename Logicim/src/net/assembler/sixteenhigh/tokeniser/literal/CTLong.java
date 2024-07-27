package net.assembler.sixteenhigh.tokeniser.literal;

public class CTLong
    extends CTIntegerLiteral
{
  protected long rawValue;

  public CTLong(long rawValue, boolean unsigned, boolean negative)
  {
    super(unsigned, negative);
    this.rawValue = rawValue;
  }

  public long getRawValue()
  {
    return rawValue;
  }

  @Override
  public boolean isValid()
  {
    return true;
  }

  @Override
  public PrimitiveTypeCode getPrimitiveTypeCode()
  {
    if (isUnsigned())
    {
      return PrimitiveTypeCode.uint64;
    }
    else
    {
      return PrimitiveTypeCode.int64;
    }
  }
}


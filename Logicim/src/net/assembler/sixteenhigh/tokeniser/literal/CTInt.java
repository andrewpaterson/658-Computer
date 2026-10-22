package net.assembler.sixteenhigh.tokeniser.literal;

public class CTInt
    extends CTIntegerLiteral
{
  public static final long MAX_UINT = 0xffffffffL;
  public static final long MIN_UINT = 0x0;
  public static final long MAX_INT = 0x7fffffffL;
  public static final long MIN_INT = -0x80000000L;

  protected long rawValue;

  public CTInt(long rawValue, boolean unsigned, boolean negative)
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
    long value = getValue();
    if (isUnsigned())
    {
      if ((value >= MIN_UINT) && (value <= MAX_UINT))
      {
        return true;
      }
      return false;
    }
    else
    {
      if ((value >= MIN_INT) && (value <= MAX_INT))
      {
        return true;
      }
      return false;
    }
  }

  @Override
  public PrimitiveTypeCode getPrimitiveTypeCode()
  {
    if (isUnsigned())
    {
      return PrimitiveTypeCode.uint32;
    }
    else
    {
      return PrimitiveTypeCode.int32;
    }
  }
}


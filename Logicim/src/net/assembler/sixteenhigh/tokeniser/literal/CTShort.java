package net.assembler.sixteenhigh.tokeniser.literal;

public class CTShort
    extends CTIntegerLiteral
{
  public static final long MAX_USHORT = 0xffff;
  public static final long MIN_USHORT = 0x0;
  public static final long MAX_SHORT = 0x7fff;
  public static final long MIN_SHORT = -0x8000;

  protected long rawValue;

  public CTShort(long rawValue, boolean unsigned, boolean negative)
  {
    super(unsigned, negative);
    this.rawValue = rawValue;
  }

  @Override
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
      if ((value >= MIN_USHORT) && (value <= MAX_USHORT))
      {
        return true;
      }
      return false;
    }
    else
    {
      if ((value >= MIN_SHORT) && (value <= MAX_SHORT))
      {
        return true;
      }
      return false;
    }
  }
}


package net.assembler.sixteenhigh.tokeniser.literal;

public class CTChar
    extends CTIntegerLiteral
{
  public static final long MAX_UCHAR = 0xff;
  public static final long MIN_UCHAR = 0x0;
  public static final long MAX_CHAR = 0x7f;
  public static final long MIN_CHAR = -0x80;

  protected long rawValue;

  public CTChar(long rawValue, boolean unsigned, boolean negative)
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
      if ((value >= MIN_UCHAR) && (value <= MAX_UCHAR))
      {
        return true;
      }
      return false;
    }
    else
    {
      if ((value >= MIN_CHAR) && (value <= MAX_CHAR))
      {
        return true;
      }
      return false;
    }
  }
}


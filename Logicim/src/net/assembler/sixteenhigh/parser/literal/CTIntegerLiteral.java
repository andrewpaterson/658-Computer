package net.assembler.sixteenhigh.parser.literal;

public abstract class CTIntegerLiteral
    extends CTLiteral
{
  protected boolean unsigned;
  protected boolean negative;

  public CTIntegerLiteral(boolean unsigned, boolean negative)
  {
    this.unsigned = unsigned;
    this.negative = negative;
  }

  public boolean isUnsigned()
  {
    return unsigned;
  }

  public boolean isPositive()
  {
    return !negative;
  }

  public long getValue()
  {
    long rawValue = getRawValue();
    if (unsigned)
    {
      return rawValue;
    }
    else
    {
      if (isPositive())
      {
        return rawValue;
      }
      else
      {
        return -rawValue;
      }
    }
  }

  public abstract long getRawValue();

  public abstract boolean isValid();
}


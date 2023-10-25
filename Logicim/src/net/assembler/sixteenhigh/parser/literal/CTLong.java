package net.assembler.sixteenhigh.parser.literal;

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
}


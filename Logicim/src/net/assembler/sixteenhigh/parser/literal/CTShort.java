package net.assembler.sixteenhigh.parser.literal;

public class CTShort extends CTLiteral
{
  protected long value;
  protected boolean unsigned;

  public CTShort(long value, boolean unsigned)
  {
    this.value = value;
    this.unsigned = unsigned;
  }

  public long getValue()
  {
    return value;
  }

  public boolean isUnsigned()
  {
    return unsigned;
  }
}


package net.assembler.sixteenhigh.parser.literal;

public class CTChar
    extends CTLiteral
{
  protected char value;
  protected boolean unsigned;

  public CTChar(char value, boolean unsigned)
  {
    this.value = value;
    this.unsigned = unsigned;
  }

  public char getValue()
  {
    return value;
  }

  public boolean isUnsigned()
  {
    return unsigned;
  }
}


package net.assembler.sixteenhigh.parser.literal;

public abstract class CTFloatingLiteral
    extends CTLiteral
{
  public abstract double getValue();

  public abstract boolean isValid();
}


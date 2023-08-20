package net.assembler.sixteenhigh.parser.statment;

public class Label
    extends Statement
{
  protected String name;

  public Label(int index, String name)
  {
    super(index);
    this.name = name;
  }
}


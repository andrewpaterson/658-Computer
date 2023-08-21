package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;

import java.util.ArrayList;
import java.util.List;

public class Routine
    extends Statement
{
  protected String name;
  protected List<LocalVariable> localVariables;

  public Routine(Code code, int index, String name)
  {
    super(code, index);
    this.name = name;
    this.localVariables = new ArrayList<>();
  }

  public void add(LocalVariable variable)
  {
    localVariables.add(variable);
  }
}


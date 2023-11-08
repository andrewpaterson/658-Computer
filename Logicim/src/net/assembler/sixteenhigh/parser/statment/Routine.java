package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;

import java.util.ArrayList;
import java.util.List;

public abstract class Routine
    extends Statement
{
  protected String name;
  protected List<LocalVariable> localVariables;
  protected List<Label> localLabels;

  public Routine(Code code, int index, String name)
  {
    super(code, index);
    this.name = name;
    this.localVariables = new ArrayList<>();
    this.localLabels = new ArrayList<>();
  }

  public void addLocalVariable(LocalVariable variable)
  {
    localVariables.add(variable);
  }

  public void addLocalLabel(Label label)
  {
    localLabels.add(label);
  }

  public boolean isRoutine()
  {
    return true;
  }
}


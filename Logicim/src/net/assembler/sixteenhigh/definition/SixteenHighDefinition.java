package net.assembler.sixteenhigh.definition;

import net.assembler.sixteenhigh.semanticiser.Routine;
import net.assembler.sixteenhigh.semanticiser.UnitDefinition;
import net.assembler.sixteenhigh.semanticiser.VariableDefinition;

import java.util.ArrayList;
import java.util.List;

public class SixteenHighDefinition
{
  protected List<Routine> globalRoutines;
  protected List<VariableDefinition> globalVariables;
  protected List<UnitDefinition> units;

  public SixteenHighDefinition()
  {
    units = new ArrayList<>();
  }
}


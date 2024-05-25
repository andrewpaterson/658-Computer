package net.assembler.sixteenhigh.definition;

import net.assembler.sixteenhigh.semanticiser.UnitDefinition;
import net.common.SimulatorException;

import java.util.LinkedHashMap;
import java.util.Map;

public class UnitDefinitions
{
  protected Map<String, UnitDefinition> units;

  public UnitDefinitions()
  {
    this.units = new LinkedHashMap<>();
  }

  public UnitDefinition get(String filename)
  {
    return units.get(filename);
  }

  public UnitDefinition createUnit(String filename)
  {
    UnitDefinition existing = units.get(filename);
    if (existing != null)
    {
      throw new SimulatorException("A Unit for filename [%s] already exists.", filename);
    }

    UnitDefinition unitDefinition = new UnitDefinition(filename);
    units.put(filename, unitDefinition);
    return unitDefinition;
  }
}


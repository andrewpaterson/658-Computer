package net.logicim.ui.simulation;

import net.logicim.domain.CircuitSimulation;

public class TopLevelSubcircuitSimulation
{
  protected SubcircuitEditor subcircuitEditor;
  protected CircuitSimulation circuitSimulation;
  protected String name;

  public TopLevelSubcircuitSimulation(SubcircuitEditor subcircuitEditor, CircuitSimulation circuitSimulation)
  {
    this.subcircuitEditor = subcircuitEditor;
    this.circuitSimulation = circuitSimulation;
    this.name = "";
  }

  public SubcircuitEditor getSubcircuitEditor()
  {
    return subcircuitEditor;
  }

  public CircuitSimulation getCircuitSimulation()
  {
    return circuitSimulation;
  }

  public String getName()
  {
    return name;
  }
}


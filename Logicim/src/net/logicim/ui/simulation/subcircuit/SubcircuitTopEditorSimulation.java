package net.logicim.ui.simulation.subcircuit;

import net.logicim.domain.CircuitSimulation;

public class SubcircuitTopEditorSimulation
{
  protected SubcircuitEditor subcircuitEditor;
  protected SubcircuitTopSimulation subcircuitTopSimulation;

  public SubcircuitTopEditorSimulation(SubcircuitEditor subcircuitEditor, SubcircuitTopSimulation subcircuitTopSimulation)
  {
    this.subcircuitEditor = subcircuitEditor;
    this.subcircuitTopSimulation = subcircuitTopSimulation;
  }

  public SubcircuitEditor getSubcircuitEditor()
  {
    return subcircuitEditor;
  }

  public SubcircuitTopSimulation getSubcircuitTopSimulation()
  {
    return subcircuitTopSimulation;
  }

  public CircuitSimulation getCircuitSimulation()
  {
    return subcircuitTopSimulation.getCircuitSimulation();
  }
}


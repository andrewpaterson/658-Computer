package net.logicim.ui.simulation.navigation;

import net.logicim.ui.circuit.path.ViewPathCircuitSimulation;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

public class SubcircuitSimulationPair
{
  xxx
  public SubcircuitEditor subcircuitEditor;
  public ViewPathCircuitSimulation pathCircuitSimulation;

  public SubcircuitSimulationPair(SubcircuitEditor subcircuitEditor, ViewPathCircuitSimulation pathCircuitSimulation)
  {
    this.subcircuitEditor = subcircuitEditor;
    this.pathCircuitSimulation = pathCircuitSimulation;
  }

  public boolean equals(SubcircuitSimulationPair obj)
  {
    return subcircuitEditor == obj.subcircuitEditor &&
           pathCircuitSimulation.equals(obj.pathCircuitSimulation);
  }
}


package net.logicim.ui.simulation.navigation;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

public class SubcircuitSimulationPair
{
  public SubcircuitEditor subcircuitEditor;
  public SubcircuitSimulation subcircuitSimulation;

  public SubcircuitSimulationPair(SubcircuitEditor subcircuitEditor, SubcircuitSimulation subcircuitSimulation)
  {
    this.subcircuitEditor = subcircuitEditor;
    this.subcircuitSimulation = subcircuitSimulation;
  }

  public boolean equals(SubcircuitSimulationPair obj)
  {
    return subcircuitEditor == obj.subcircuitEditor &&
           subcircuitSimulation == obj.subcircuitSimulation;
  }
}


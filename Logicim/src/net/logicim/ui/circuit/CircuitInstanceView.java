package net.logicim.ui.circuit;

import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

public interface CircuitInstanceView
{
  SubcircuitView getCircuitSubcircuitView();

  void destroyCircuitSimulation(CircuitSimulation circuitSimulation);
}


package net.logicim.ui.circuit;

import net.logicim.domain.CircuitSimulation;

public interface CircuitInstanceView
{
  SubcircuitView getCircuitSubcircuitView();

  void destroyComponents(CircuitSimulation circuitSimulation);
}


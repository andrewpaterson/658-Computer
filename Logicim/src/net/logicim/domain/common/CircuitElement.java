package net.logicim.domain.common;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

public interface CircuitElement
  extends Described
{
  String getType();

  SubcircuitSimulation getContainingSubcircuitSimulation();
}


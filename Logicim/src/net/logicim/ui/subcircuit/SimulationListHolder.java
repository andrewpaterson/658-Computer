package net.logicim.ui.subcircuit;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.List;

public interface SimulationListHolder
{
  List<? extends SubcircuitSimulation> getSubcircuitSimulations();

  SubcircuitSimulation getCurrentSimulation();
}


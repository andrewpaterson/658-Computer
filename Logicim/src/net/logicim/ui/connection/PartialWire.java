package net.logicim.ui.connection;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.List;
import java.util.Map;

public class PartialWire
{
  protected Map<SubcircuitSimulation, List<WireConnection>> connectedWires;

  public PartialWire(Map<SubcircuitSimulation, List<WireConnection>> connectedWires)
  {
    this.connectedWires = connectedWires;
  }
}

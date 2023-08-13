package net.logicim.ui.connection;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceView;

import java.util.List;
import java.util.Map;

public class PartialWire
{
  protected Map<CircuitInstanceView, List<WireConnection>> connectedWires;

  public PartialWire(Map<CircuitInstanceView, List<WireConnection>> connectedWires)
  {
    this.connectedWires = connectedWires;
  }
}


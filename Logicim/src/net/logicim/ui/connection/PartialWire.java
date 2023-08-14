package net.logicim.ui.connection;

import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.CircuitInstanceViewPath;

import java.util.List;
import java.util.Map;

public class PartialWire
{
  protected Map<CircuitInstanceViewPath, List<WireConnection>> connectedWires;

  public PartialWire(Map<CircuitInstanceViewPath, List<WireConnection>> connectedWires)
  {
    this.connectedWires = connectedWires;
  }
}


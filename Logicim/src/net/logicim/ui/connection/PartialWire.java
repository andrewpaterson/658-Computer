package net.logicim.ui.connection;

import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceViewPath;

import java.util.List;
import java.util.Map;

public class PartialWire
{
  protected Map<CircuitInstanceViewPath, List<WireViewPathConnection>> connectedWires;

  public PartialWire(Map<CircuitInstanceViewPath, List<WireViewPathConnection>> connectedWires)
  {
    this.connectedWires = connectedWires;
  }

  public boolean containsCircuitSimulation(CircuitSimulation circuitSimulation)
  {
    for (CircuitInstanceViewPath path : connectedWires.keySet())
    {
      if (path.containsCircuitSimulation(circuitSimulation))
      {
        return true;
      }
    }
    return false;
  }
}


package net.logicim.ui.connection;

import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;

import java.util.List;
import java.util.Map;

public class PartialWire
{
  protected Map<ViewPath, List<WireViewPathConnection>> connectedWires;

  public PartialWire(Map<ViewPath, List<WireViewPathConnection>> connectedWires)
  {
    this.connectedWires = connectedWires;
  }

  public boolean containsCircuitSimulation(CircuitSimulation circuitSimulation)
  {
    for (ViewPath viewPath : connectedWires.keySet())
    {
      if (viewPath.containsCircuitSimulation(circuitSimulation))
      {
        return true;
      }
    }
    return false;
  }
}


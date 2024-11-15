package net.logicim.ui.simulation.navigation;

import net.logicim.ui.Stack;
import net.logicim.ui.circuit.path.ViewPathCircuitSimulation;

public class NavigationStack
    extends Stack<ViewPathCircuitSimulation>
{
  public NavigationStack()
  {
    super();
  }

  @Override
  public void push(ViewPathCircuitSimulation subcircuitSimulationPair)
  {
    ViewPathCircuitSimulation peek = peek();
    if (peek != null && peek.equals(subcircuitSimulationPair))
    {
      return;
    }
    super.push(subcircuitSimulationPair);
  }
}


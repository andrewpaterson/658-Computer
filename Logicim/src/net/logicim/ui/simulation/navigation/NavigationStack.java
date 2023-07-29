package net.logicim.ui.simulation.navigation;

import net.logicim.ui.Stack;

public class NavigationStack
    extends Stack<SubcircuitSimulationPair>
{
  public NavigationStack()
  {
    super();
  }

  @Override
  public void push(SubcircuitSimulationPair subcircuitSimulationPair)
  {
    SubcircuitSimulationPair peek = peek();
    if (peek != null && peek.equals(subcircuitSimulationPair))
    {
      return;
    }
    super.push(subcircuitSimulationPair);
  }
}


package net.logicim.ui.circuit.path;

import net.logicim.domain.CircuitSimulation;

public class ViewPathCircuitSimulation
{
  protected ViewPath viewPath;
  protected CircuitSimulation circuitSimulation;

  public ViewPathCircuitSimulation(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
    this.viewPath = viewPath;
    this.circuitSimulation = circuitSimulation;
  }

  public ViewPath getViewPath()
  {
    return viewPath;
  }

  public CircuitSimulation getCircuitSimulation()
  {
    return circuitSimulation;
  }

  public boolean equals(ViewPathCircuitSimulation obj)
  {
    return (circuitSimulation == obj.circuitSimulation) &&
           (viewPath == obj.viewPath);
  }
}


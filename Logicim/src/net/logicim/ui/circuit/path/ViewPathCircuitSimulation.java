package net.logicim.ui.circuit.path;

import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

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

  public SubcircuitEditor getSubcircuitEditor()
  {
    CircuitInstanceView last = viewPath.getLast();
    SubcircuitView subcircuitView = last.getInstanceSubcircuitView();
    return (SubcircuitEditor) subcircuitView.getSubcircuitEditor();
  }
}


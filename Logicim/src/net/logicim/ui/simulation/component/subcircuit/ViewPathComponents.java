package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.circuit.path.ViewPathCircuitSimulation;
import net.logicim.ui.common.integratedcircuit.ComponentView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ViewPathComponents
{
  protected ViewPathCircuitSimulation viewPathCircuitSimulation;

  protected List<ComponentView<?>> componentViews;

  public ViewPathComponents(ViewPathCircuitSimulation viewPathCircuitSimulation, Collection<ComponentView<?>> componentViews)
  {
    this.viewPathCircuitSimulation = viewPathCircuitSimulation;
    this.componentViews = new ArrayList<>(componentViews);
  }

  public CircuitSimulation getCircuitSimulation()
  {
    return viewPathCircuitSimulation.getCircuitSimulation();
  }

  public List<ComponentView<?>> getComponentViews()
  {
    return componentViews;
  }

  public ViewPath getViewPath()
  {
    return viewPathCircuitSimulation.getViewPath();
  }
}


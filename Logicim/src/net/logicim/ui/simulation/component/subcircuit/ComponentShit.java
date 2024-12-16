package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.integratedcircuit.ComponentView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ComponentShit
{
  protected CircuitSimulation circuitSimulation;
  protected List<ComponentView<?>> componentViews;
  protected ViewPath viewPath;

  public ComponentShit(CircuitSimulation circuitSimulation, ViewPath viewPath,Collection<ComponentView<?>> componentViews)
  {
    this.circuitSimulation = circuitSimulation;
    this.viewPath = viewPath;
    this.componentViews = new ArrayList<>(componentViews);
  }

  public CircuitSimulation getCircuitSimulation()
  {
    return circuitSimulation;
  }

  public List<ComponentView<?>> getComponentViews()
  {
    return componentViews;
  }

  public ViewPath getViewPath()
  {
    return viewPath;
  }
}

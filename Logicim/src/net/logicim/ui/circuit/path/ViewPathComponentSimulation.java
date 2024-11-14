package net.logicim.ui.circuit.path;

import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Component;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewPathComponentSimulation<COMPONENT extends Component>
{
  protected Map<ViewPath, Map<CircuitSimulation, COMPONENT>> map;

  public ViewPathComponentSimulation()
  {
    this.map = new LinkedHashMap<>();
  }
}


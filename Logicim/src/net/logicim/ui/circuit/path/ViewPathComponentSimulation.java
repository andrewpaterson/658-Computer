package net.logicim.ui.circuit.path;

import net.logicim.domain.common.Component;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewPathComponentSimulation<COMPONENT extends Component>
{
  protected Map<ViewPath, Map<SubcircuitSimulation, COMPONENT>> map;

  public ViewPathComponentSimulation()
  {
    this.map = new LinkedHashMap<>();
  }
}


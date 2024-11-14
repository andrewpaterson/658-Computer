package net.logicim.ui.circuit.path;

import net.logicim.domain.common.Component;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.LinkedHashMap;
import java.util.Map;

public class CircuitInstanceViewPathComponentSimulation<COMPONENT extends Component>
{
  protected Map<CircuitInstanceViewPath, Map<SubcircuitSimulation, COMPONENT>> map;

  public CircuitInstanceViewPathComponentSimulation()
  {
    this.map = new LinkedHashMap<>();
  }
}


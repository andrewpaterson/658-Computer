package net.logicim.ui.connection;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;

public class ComponentSimulationConnection<T extends View>
{
  protected T component;
  protected SubcircuitSimulation subcircuitSimulation;
  protected ConnectionView connection;

  public ComponentSimulationConnection(T component,
                                       SubcircuitSimulation subcircuitSimulation,
                                       ConnectionView connection)
  {
    this.component = component;
    this.subcircuitSimulation = subcircuitSimulation;
    this.connection = connection;
  }
}


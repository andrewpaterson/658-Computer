package net.logicim.ui.connection;

import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;

public class ComponentSimulationConnection<T extends View>
{
  protected T component;
  protected CircuitInstanceView circuitInstanceView;
  protected ConnectionView connection;

  public ComponentSimulationConnection(T component,
                                       CircuitInstanceView circuitInstanceView,
                                       ConnectionView connection)
  {
    this.component = component;
    this.circuitInstanceView = circuitInstanceView;
    this.connection = connection;
  }
}


package net.logicim.ui.connection;

import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;

public class SplitterProcessStackItem
{
  protected SplitterView component;
  protected CircuitInstanceView circuitInstanceView;
  protected ConnectionView connection;

  public SplitterProcessStackItem(SplitterView component,
                                  CircuitInstanceView circuitInstanceView,
                                  ConnectionView connection)
  {
    this.component = component;
    this.circuitInstanceView = circuitInstanceView;
    this.connection = connection;
  }
}


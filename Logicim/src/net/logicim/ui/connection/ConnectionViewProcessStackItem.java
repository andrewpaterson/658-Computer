package net.logicim.ui.connection;

import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.common.ConnectionView;

public class ConnectionViewProcessStackItem
{
  public CircuitInstanceView circuitInstanceView;
  public ConnectionView inputConnectionView;

  public ConnectionViewProcessStackItem(CircuitInstanceView circuitInstanceView, ConnectionView inputConnectionView)
  {
    this.circuitInstanceView = circuitInstanceView;
    this.inputConnectionView = inputConnectionView;
  }
}


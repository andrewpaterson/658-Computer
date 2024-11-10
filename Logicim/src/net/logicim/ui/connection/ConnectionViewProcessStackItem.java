package net.logicim.ui.connection;

import net.logicim.ui.circuit.path.CircuitInstanceViewPath;
import net.logicim.ui.common.ConnectionView;

public class ConnectionViewProcessStackItem
{
  public CircuitInstanceViewPath circuitInstanceViewPath;
  public ConnectionView inputConnectionView;

  public ConnectionViewProcessStackItem(CircuitInstanceViewPath circuitInstanceViewPath, ConnectionView inputConnectionView)
  {
    this.circuitInstanceViewPath = circuitInstanceViewPath;
    this.inputConnectionView = inputConnectionView;
  }
}


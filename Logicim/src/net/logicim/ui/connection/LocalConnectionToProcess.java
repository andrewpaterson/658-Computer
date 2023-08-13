package net.logicim.ui.connection;

import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.common.ConnectionView;

public class LocalConnectionToProcess
{
  public CircuitInstanceView circuitInstanceView;
  public ConnectionView inputConnectionView;

  public LocalConnectionToProcess(CircuitInstanceView circuitInstanceView, ConnectionView inputConnectionView)
  {
    this.circuitInstanceView = circuitInstanceView;
    this.inputConnectionView = inputConnectionView;
  }
}


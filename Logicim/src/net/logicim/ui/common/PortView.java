package net.logicim.ui.common;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.port.Port;

public class PortView
{
  protected IntegratedCircuitView<?> integratedCircuitView;
  protected Port port;
  protected Int2D position;

  public PortView(IntegratedCircuitView<?> integratedCircuitView, Port port, Int2D position)
  {
    this.integratedCircuitView = integratedCircuitView;
    this.port = port;
    this.position = position;
    this.integratedCircuitView.addPortView(this);
  }

  public Port getPort()
  {
    return port;
  }
}


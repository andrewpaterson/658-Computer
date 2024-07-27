package net.logicim.ui.connection;

import net.logicim.ui.common.integratedcircuit.ComponentView;

public class ComponentViewPortName
{
  public ComponentView<?> componentView;
  public String portName;

  public ComponentViewPortName(ComponentView<?> componentView, String portName)
  {
    this.componentView = componentView;
    this.portName = portName;
  }
}


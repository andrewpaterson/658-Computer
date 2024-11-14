package net.logicim.ui.connection;

import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.integratedcircuit.ComponentView;

public class ComponentViewPortName
{
  protected ComponentView<?> componentView;
  protected String portName;
  protected ViewPath path;

  public ComponentViewPortName(ComponentView<?> componentView,
                               String portName,
                               ViewPath path)
  {
    this.componentView = componentView;
    this.portName = portName;
    this.path = path;
  }

  public ComponentView<?> getComponentView()
  {
    return componentView;
  }

  public String getPortName()
  {
    return portName;
  }

  public ViewPath getPath()
  {
    return path;
  }

  public boolean isComponent(ComponentView<?> componentView)
  {
    return this.componentView == componentView;
  }

  public boolean isPortName(String portName)
  {
    return this.portName.equals(portName);
  }

  public boolean equals(ComponentView<?> componentView, String portName, ViewPath path)
  {
    if ((this.componentView == componentView) &&
        this.portName.equals(portName) &&
        (this.path == path))
    {
      return true;
    }
    return false;
  }
}


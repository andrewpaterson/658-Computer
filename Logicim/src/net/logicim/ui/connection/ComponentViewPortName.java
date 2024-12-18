package net.logicim.ui.connection;

import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.integratedcircuit.ComponentView;

public class ComponentViewPortName
{
  protected ComponentView<?> componentView;
  protected String portName;
  protected ViewPath viewPath;

  public ComponentViewPortName(ComponentView<?> componentView,
                               String portName,
                               ViewPath viewPath)
  {
    this.componentView = componentView;
    this.portName = portName;
    this.viewPath = viewPath;
  }

  public ComponentView<?> getComponentView()
  {
    return componentView;
  }

  public String getPortName()
  {
    return portName;
  }

  public ViewPath getViewPath()
  {
    return viewPath;
  }

  public boolean isComponent(ComponentView<?> componentView)
  {
    return this.componentView == componentView;
  }

  public boolean isPortName(String portName)
  {
    return this.portName.equals(portName);
  }

  public boolean equals(ComponentView<?> componentView, String portName, ViewPath viewPath)
  {
    if ((this.componentView == componentView) &&
        this.portName.equals(portName) &&
        (this.viewPath == viewPath))
    {
      return true;
    }
    return false;
  }
}


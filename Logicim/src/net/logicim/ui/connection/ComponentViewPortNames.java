package net.logicim.ui.connection;

import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;

import java.util.ArrayList;
import java.util.List;

public class ComponentViewPortNames
{
  protected List<ComponentViewPortName> connectedPortIndices;

  public ComponentViewPortNames()
  {
    this.connectedPortIndices = new ArrayList<>();
  }

  public void addPort(ComponentView<?> componentView, String portName)
  {
    ComponentViewPortName componentViewPortName = new ComponentViewPortName(componentView, portName);
    connectedPortIndices.add(componentViewPortName);
  }

  public List<ComponentViewPortName> getConnectedPortIndices()
  {
    return connectedPortIndices;
  }

  public List<ComponentViewPortName> getSplitterPortIndices()
  {
    List<ComponentViewPortName> splitterPortIndices= new ArrayList<>();
    for (ComponentViewPortName componentViewPortName : connectedPortIndices)
    {
      ComponentView<?> componentView = componentViewPortName.componentView;
      boolean isSplitter = componentView instanceof SplitterView;
      if (isSplitter)
      {
        splitterPortIndices.add(componentViewPortName);
      }

    }
    return splitterPortIndices;
  }
}

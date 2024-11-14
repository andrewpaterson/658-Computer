package net.logicim.ui.connection;

import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;

import java.util.ArrayList;
import java.util.List;

public class ComponentViewPortNames
{
  protected LocalMultiSimulationConnectionNet multiSimulationConnectionNet;
  protected List<ComponentViewPortName> connectedPortIndices;

  public ComponentViewPortNames(LocalMultiSimulationConnectionNet multiSimulationConnectionNet)
  {
    this.multiSimulationConnectionNet = multiSimulationConnectionNet;
    this.connectedPortIndices = new ArrayList<>();
  }

  public void addPort(ComponentView<?> componentView,
                      String portName,
                      ViewPath path)
  {
    if (!contains(componentView, portName, path))
    {
      ComponentViewPortName componentViewPortName = new ComponentViewPortName(componentView, portName, path);
      connectedPortIndices.add(componentViewPortName);
    }
  }

  protected boolean contains(ComponentView<?> componentView, String portName, ViewPath path)
  {
    for (ComponentViewPortName componentViewPortName : connectedPortIndices)
    {
      if (componentViewPortName.equals(componentView, portName, path))
      {
        return true;
      }
    }
    return false;
  }

  public List<ComponentViewPortName> getConnectedPortIndices()
  {
    return connectedPortIndices;
  }

  public List<ComponentViewPortName> getSplitterPortIndices()
  {
    List<ComponentViewPortName> splitterPortIndices = new ArrayList<>();
    for (ComponentViewPortName componentViewPortName : connectedPortIndices)
    {
      ComponentView<?> componentView = componentViewPortName.getComponentView();
      boolean isSplitter = componentView instanceof SplitterView;
      if (isSplitter)
      {
        splitterPortIndices.add(componentViewPortName);
      }

    }
    return splitterPortIndices;
  }

  public LocalMultiSimulationConnectionNet getMultiSimulationConnectionNet()
  {
    return multiSimulationConnectionNet;
  }
}


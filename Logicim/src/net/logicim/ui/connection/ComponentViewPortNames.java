package net.logicim.ui.connection;

import net.logicim.domain.CircuitSimulation;
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
                      ViewPath viewPath)
  {
    if (!contains(componentView, portName, viewPath))
    {
      ComponentViewPortName componentViewPortName = new ComponentViewPortName(componentView, portName, viewPath);
      connectedPortIndices.add(componentViewPortName);
    }
  }

  protected boolean contains(ComponentView<?> componentView, String portName, ViewPath viewPath)
  {
    for (ComponentViewPortName componentViewPortName : connectedPortIndices)
    {
      if (componentViewPortName.equals(componentView, portName, viewPath))
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

  public boolean containsCircuitSimulation(CircuitSimulation circuitSimulation)
  {
    for (ComponentViewPortName connectedPortIndex : connectedPortIndices)
    {
      if (!connectedPortIndex.getViewPath().containsCircuitSimulation(circuitSimulation))
      {
        return false;
      }
    }
    return true;
  }
}


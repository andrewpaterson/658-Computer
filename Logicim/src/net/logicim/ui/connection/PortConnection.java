package net.logicim.ui.connection;

import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;

import java.util.ArrayList;
import java.util.List;

public class PortConnection
{
  protected LocalMultiSimulationConnectionNet multiSimulationConnectionNet;

  protected List<ComponentViewPortName> connectedPortIndices;
  protected List<ComponentViewPortName> splitterPortIndices;

  public PortConnection(LocalMultiSimulationConnectionNet multiSimulationConnectionNet)
  {
    this.multiSimulationConnectionNet = multiSimulationConnectionNet;
    this.connectedPortIndices = new ArrayList<>();
    this.splitterPortIndices = new ArrayList<>();
  }

  public void addPort(ComponentView<?> componentView, String portName)
  {
    ComponentViewPortName componentViewPortName = new ComponentViewPortName(componentView, portName);
    connectedPortIndices.add(componentViewPortName);

    boolean isSplitter = componentView instanceof SplitterView;
    if (isSplitter)
    {
      splitterPortIndices.add(componentViewPortName);
    }
  }

  public LocalMultiSimulationConnectionNet getMultiSimulationConnectionNet()
  {
    return multiSimulationConnectionNet;
  }

  public List<ComponentViewPortName> getConnectedPortIndices()
  {
    return connectedPortIndices;
  }

  public List<ComponentViewPortName> getSplitterPortIndices()
  {
    return splitterPortIndices;
  }
}


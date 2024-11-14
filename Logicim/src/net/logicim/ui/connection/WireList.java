package net.logicim.ui.connection;

import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.ComponentView;

import java.util.*;

public class WireList
{
  protected List<FullWire> fullWires;
  protected List<PartialWire> partialWires;

  protected List<LocalMultiSimulationConnectionNet> connectionNets;

  public WireList(List<LocalMultiSimulationConnectionNet> connectionNets)
  {
    fullWires = new ArrayList<>();
    partialWires = new ArrayList<>();

    this.connectionNets = connectionNets;
  }

  public void add(Collection<FullWire> fullWires)
  {
    this.fullWires.addAll(fullWires);
  }

  public void add(PartialWire partialWire)
  {
    partialWires.add(partialWire);
  }

  public List<FullWire> getFullWires()
  {
    return fullWires;
  }

  public List<PartialWire> getPartialWires(CircuitSimulation circuitSimulation)
  {
    List<PartialWire> result = new ArrayList<>();
    for (PartialWire partialWire : partialWires)
    {
      if (partialWire.containsCircuitSimulation(circuitSimulation))
      {
        result.add(partialWire);
      }
    }
    return result;
  }

  public List<LocalMultiSimulationConnectionNet> getConnectionNets(CircuitSimulation circuitSimulation)
  {
    List<LocalMultiSimulationConnectionNet> result = new ArrayList<>();
    for (LocalMultiSimulationConnectionNet connectionNet : connectionNets)
    {
      for (LocalConnectionNet localConnectionNet : connectionNet.localConnectionNets)
      {
        if (localConnectionNet.getPath().containsCircuitSimulation(circuitSimulation))
        {
          result.add(connectionNet);
          break;
        }
      }
    }
    return result;
  }

  public List<ConnectionView> getConnectionViews()
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (LocalMultiSimulationConnectionNet connectionNet : connectionNets)
    {
      Map<ViewPath, Set<ConnectionView>> iDontKnowWhatImDoing = connectionNet.getConnectionViews();

      if (iDontKnowWhatImDoing.size() > 1)
      {
        int xxx = 0;
      }

      for (Set<ConnectionView> value : iDontKnowWhatImDoing.values())
      {
        connectionViews.addAll(value);
      }
    }
    return new ArrayList<>(connectionViews);
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder("Connection Nets [");
    builder.append(connectionNets.size());
    builder.append("]\n");

    Map<ViewPath, List<ComponentViewPortName>> map = new LinkedHashMap<>();
    for (FullWire fullWire : fullWires)
    {
      Set<ComponentViewPortNames> localWires = fullWire.getLocalWires();
      for (ComponentViewPortNames localWire : localWires)
      {
        for (ComponentViewPortName connectedPortIndex : localWire.getConnectedPortIndices())
        {
          ViewPath path = connectedPortIndex.getPath();
          List<ComponentViewPortName> list = map.get(path);
          if (list == null)
          {
            list = new ArrayList<>();
            map.put(path, list);
          }

          list.add(connectedPortIndex);
        }
      }
    }

    for (Map.Entry<ViewPath, List<ComponentViewPortName>> entry : map.entrySet())
    {
      ViewPath path = entry.getKey();
      List<ComponentViewPortName> componentViewPortNames = entry.getValue();
      builder.append(path.getDescription());
      builder.append("\n");

      for (ComponentViewPortName componentViewPortName : componentViewPortNames)
      {
        builder.append("  ");
        ComponentView componentView = componentViewPortName.getComponentView();
        builder.append(componentView.getType());
        builder.append(" ");
        builder.append(componentView.getDescription());
        builder.append(": ");
        String portName = componentViewPortName.getPortName();
        builder.append(portName);
        builder.append("\n");
      }
    }

    return builder.toString();
  }
}


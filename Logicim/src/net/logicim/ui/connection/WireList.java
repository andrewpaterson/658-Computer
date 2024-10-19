package net.logicim.ui.connection;

import net.logicim.ui.circuit.CircuitInstanceViewPath;
import net.logicim.ui.common.ConnectionView;

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

  public List<PartialWire> getPartialWires()
  {
    return partialWires;
  }

  public List<LocalMultiSimulationConnectionNet> getConnectionNets()
  {
    return connectionNets;
  }

  public List<ConnectionView> getConnectionViews()
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (LocalMultiSimulationConnectionNet connectionNet : connectionNets)
    {
      Map<CircuitInstanceViewPath, Set<ConnectionView>> iDontKnowWhatImDoing = connectionNet.getConnectionViews();

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
}


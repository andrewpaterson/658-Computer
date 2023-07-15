package net.logicim.ui.connection;

import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;

import java.util.*;

public class LocalMultiSimulationConnectionNet
{
  protected List<LocalConnectionNet> localConnectionNets;

  protected Set<Trace> traces = new LinkedHashSet<>();

  public LocalMultiSimulationConnectionNet()
  {
    this.localConnectionNets = new ArrayList<>();
  }

  public List<WireConnection> getConnectedWires()
  {
    List<WireConnection> result = new ArrayList<>();
    for (LocalConnectionNet localConnectionNet : localConnectionNets)
    {
      List<WireConnection> connectedWires = localConnectionNet.getConnectedWires();
      result.addAll(connectedWires);
    }
    return result;
  }

  public void add(LocalConnectionNet localConnectionNet)
  {
    localConnectionNets.add(localConnectionNet);
  }

  public List<PortConnection> getPortConnections()
  {
    List<PortConnection> result = new ArrayList<>();
    for (LocalConnectionNet localConnectionNet : localConnectionNets)
    {
      List<PortConnection> portConnections = localConnectionNet.getPortConnections();
      result.addAll(portConnections);
    }
    return result;
  }

  public List<SplitterView> getSplitterViews()
  {
    List<SplitterView> result = new ArrayList<>();
    for (LocalConnectionNet localConnectionNet : localConnectionNets)
    {
      List<ComponentConnection<SplitterView>> connectionNetSplitterViews = localConnectionNet.getSplitterViews();
      for (ComponentConnection<SplitterView> connectionNetSplitterView : connectionNetSplitterViews)
      {
        result.add(connectionNetSplitterView.component);
      }
    }
    return result;
  }

  public Collection<ConnectionView> getConnectionViews()
  {
    List<ConnectionView> result = new ArrayList<>();
    for (LocalConnectionNet localConnectionNet : localConnectionNets)
    {
      List<ConnectionView> connectionViews = localConnectionNet.getConnectionViews();
      result.addAll(connectionViews);
    }
    return result;
  }

  public void addTrace(Trace trace)
  {
    traces.add(trace);
  }

  public List<Trace> getTraces()
  {
    return new ArrayList<>(traces);
  }
}


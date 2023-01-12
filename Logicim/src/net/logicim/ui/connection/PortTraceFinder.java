package net.logicim.ui.connection;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.wire.WireView;
import net.logicim.ui.integratedcircuit.standard.passive.splitter.SplitterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortTraceFinder
{
  protected Simulation simulation;

  protected List<LocalConnectionNet> connectionNets;
  protected List<ComponentConnection<SplitterView>> splitterViewStack;
  protected Map<ConnectionView, SplitterView> processedSplitterViewConnections;
  protected int stackIndex;

  public PortTraceFinder(Simulation simulation)
  {
    this.simulation = simulation;
    connectionNets = new ArrayList<>();
    splitterViewStack = new ArrayList<>();
    processedSplitterViewConnections = new HashMap<>();
    stackIndex = 0;
  }

  public void findAndConnectTraces(ConnectionView inputConnectionView)
  {
    if (inputConnectionView == null)
    {
      throw new SimulatorException("Input connection may not be null.");
    }

    processLocalConnections(inputConnectionView);

    while (stackIndex < splitterViewStack.size())
    {
      ComponentConnection<SplitterView> splitterViewConnection = splitterViewStack.get(stackIndex);
      processLocalConnections(splitterViewConnection.connection);

      stackIndex++;
    }

    for (LocalConnectionNet connectionNet : connectionNets)
    {
      List<PortConnections> portConnections = connectionNet.getPortConnections();
      List<Trace> traces = new ArrayList<>();
      for (PortConnections portConnection : portConnections)
      {
        Trace trace = new Trace();
        traces.add(trace);
        List<Port> ports = portConnection.getConnectedPorts();
        for (Port port : ports)
        {
          port.disconnect(simulation);
          port.connect(trace);
        }

        List<ComponentConnection<WireView>> wireConnections = connectionNet.getConnectedWires();
        for (ComponentConnection<WireView> wireConnection : wireConnections)
        {
          WireView wireView = wireConnection.component;
          wireView.connectTraces(traces);
        }
      }
    }
  }

  protected void processLocalConnections(ConnectionView inputConnectionView)
  {
    LocalConnectionNet connectionNet = new LocalConnectionNet(inputConnectionView);
    connectionNets.add(connectionNet);

    List<ComponentConnection<SplitterView>> splitterViews = connectionNet.getSplitterViews();
    for (ComponentConnection<SplitterView> splitterViewConnection : splitterViews)
    {
      SplitterView splitterView = splitterViewConnection.component;
      ConnectionView connection = splitterViewConnection.connection;
      processedSplitterViewConnections.put(connection, splitterView);
    }

    for (ComponentConnection<SplitterView> splitterViewConnection : splitterViews)
    {
      SplitterView splitterView = splitterViewConnection.component;

      List<PortView> portViews = splitterView.getPorts();
      for (PortView portView : portViews)
      {
        ConnectionView connectionView = portView.getConnection();
        if (!processedSplitterViewConnections.containsKey(connectionView))
        {
          splitterViewStack.add(new ComponentConnection<>(splitterView, connectionView));
        }
      }
    }
  }

  public List<PortView> getPortViews()
  {
    ArrayList<PortView> portViews = new ArrayList<>();
    for (LocalConnectionNet connectionNet : connectionNets)
    {
      portViews.addAll(connectionNet.getPortViews());
    }

    return portViews;
  }
}


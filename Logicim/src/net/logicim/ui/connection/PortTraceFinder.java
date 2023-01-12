package net.logicim.ui.connection;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.wire.WireView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;

import java.util.*;

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
      ConnectionView connectionView = splitterViewConnection.connection;
      if (!processedSplitterViewConnections.containsKey(connectionView))
      {
        processLocalConnections(connectionView);
      }

      stackIndex++;
    }

    Map<Port, Port> totalSplitterPortMap = createSplitterPortMap();
    Map<Port, WireConnection> totalPortWireMap = createPortWireMap();

    Set<WireConnection> processedWires = new HashSet<>();

    List<FullWire> fullWires = new ArrayList<>();
    for (LocalConnectionNet connectionNet : connectionNets)
    {
      List<WireConnection> wireConnections = connectionNet.getWireConnections();
      for (WireConnection wireConnection : wireConnections)
      {
        if (!processedWires.contains(wireConnection))
        {
          FullWire fullWire = new FullWire();
          fullWire.process(wireConnection);
          processedWires.add(wireConnection);
          while (fullWire.hasPortToProcess())
          {
            Port port = fullWire.getNextPort();
            Port oppositeSplitterPort = totalSplitterPortMap.get(port);
            WireConnection wireConnection1 = totalPortWireMap.get(oppositeSplitterPort);
            if (!processedWires.contains(wireConnection1))
            {
              fullWire.process(wireConnection1);
              processedWires.add(wireConnection1);
            }
          }
          fullWire.done();
          fullWires.add(fullWire);
        }
      }
    }

    for (FullWire fullWire : fullWires)
    {
      Trace trace = new Trace();
      Set<WireConnection> localWires = fullWire.getLocalWires();
      for (WireConnection localWire : localWires)
      {
        for (Port port : localWire.connectedPorts)
        {
          port.disconnect(simulation);
          port.connect(trace);
        }
        localWire.localConnectionNet.addTrace(trace);
      }
    }

    for (LocalConnectionNet connectionNet : connectionNets)
    {
      for (ComponentConnection<WireView> connectedWire : connectionNet.getConnectedWires())
      {
        WireView wireView = connectedWire.component;
        wireView.connectTraces(connectionNet.getTraces());
      }
    }
  }

  protected Map<Port, Port> createSplitterPortMap()
  {
    Map<Port, Port> totalSplitterPortMap = new HashMap<>();
    for (LocalConnectionNet connectionNet : connectionNets)
    {
      List<ComponentConnection<SplitterView>> splitterViewConnections = connectionNet.getSplitterViews();
      for (ComponentConnection<SplitterView> splitterViewConnection : splitterViewConnections)
      {
        SplitterView splitterView = splitterViewConnection.component;
        Map<Port, Port> portMap = splitterView.getBidirectionalPortMap();
        totalSplitterPortMap.putAll(portMap);
      }
    }
    return totalSplitterPortMap;
  }

  protected Map<Port, WireConnection> createPortWireMap()
  {
    Map<Port, WireConnection> totalPortWireMap = new HashMap<>();
    for (LocalConnectionNet connectionNet : connectionNets)
    {
      List<WireConnection> wireConnections = connectionNet.getWireConnections();
      for (WireConnection wireConnection : wireConnections)
      {
        Set<Port> splitterPorts = wireConnection.splitterPorts;
        for (Port splitterPort : splitterPorts)
        {
          totalPortWireMap.put(splitterPort, wireConnection);
        }
      }
    }
    return totalPortWireMap;
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


package net.logicim.ui.connection;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LocalConnectionNet
{
  protected LocalMultiSimulationConnectionNet multiSimulationConnectionNet;
  protected SubcircuitSimulation subcircuitSimulation;
  protected CircuitInstanceView circuitInstanceView;
  protected Set<ConnectionView> connectionViews;
  protected List<ComponentConnection<SubcircuitInstanceView>> subcircuitInstanceViews;
  protected List<ComponentConnection<PinView>> pinViews;
  protected List<ComponentSimulationConnection<SplitterView>> splitterViews;

  public LocalConnectionNet(SubcircuitSimulation subcircuitSimulation,
                            CircuitInstanceView circuitInstanceView,
                            LocalMultiSimulationConnectionNet multiSimulationConnectionNet)
  {
    this.subcircuitSimulation = subcircuitSimulation;
    this.circuitInstanceView = circuitInstanceView;
    this.multiSimulationConnectionNet = multiSimulationConnectionNet;
    this.multiSimulationConnectionNet.add(this);

    this.subcircuitInstanceViews = new ArrayList<>();
    this.pinViews = new ArrayList<>();
    this.splitterViews = new ArrayList<>();

    this.connectionViews = new LinkedHashSet<>();
  }

  protected void process(ConnectionView inputConnectionView)
  {
    ConnectionFinder connectionFinder = new ConnectionFinder();
    connectionFinder.addConnection(inputConnectionView);
    connectionFinder.process();
    this.connectionViews.addAll(connectionFinder.getConnections());

    for (ConnectionView connectionView : connectionViews)
    {
      List<View> localConnected = connectionView.getConnectedComponents();
      for (View connectedView : localConnected)
      {
        if (connectedView instanceof SubcircuitInstanceView)
        {
          subcircuitInstanceViews.add(new ComponentConnection<>((SubcircuitInstanceView) connectedView, connectionView));
        }
        else if (connectedView instanceof SplitterView)
        {
          //This subcircuitSimulation is problematic.
          splitterViews.add(new ComponentSimulationConnection<>((SplitterView) connectedView,
                                                                subcircuitSimulation,
                                                                circuitInstanceView,
                                                                connectionView));
        }
        else if (connectedView instanceof PinView)
        {
          pinViews.add(new ComponentConnection<>((PinView) connectedView, connectionView));
        }
      }
    }
  }

  public Set<ConnectionView> getConnectionViews()
  {
    return connectionViews;
  }

  public SubcircuitSimulation getSubcircuitSimulation()
  {
    return subcircuitSimulation;
  }

  public List<ComponentSimulationConnection<SplitterView>> getSplitterViews()
  {
    return splitterViews;
  }
}


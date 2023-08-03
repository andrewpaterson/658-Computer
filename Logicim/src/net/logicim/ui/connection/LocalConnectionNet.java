package net.logicim.ui.connection;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LocalConnectionNet
{
  protected LocalMultiSimulationConnectionNet multiSimulationConnectionNet;
  protected SubcircuitSimulation subcircuitSimulation;
  protected Set<ConnectionView> connectionViews;
  protected List<ComponentConnection<SubcircuitInstanceView>> subcircuitInstanceViews;
  protected List<ComponentConnection<PinView>> pinViews;
  protected List<ComponentSimulationConnection<SplitterView>> splitterViews;

  public LocalConnectionNet(SubcircuitSimulation subcircuitSimulation,
                            LocalMultiSimulationConnectionNet multiSimulationConnectionNet,
                            ConnectionView inputConnectionView)
  {
    this.subcircuitSimulation = subcircuitSimulation;
    this.multiSimulationConnectionNet = multiSimulationConnectionNet;
    this.multiSimulationConnectionNet.add(this);

    ConnectionFinder connectionFinder = new ConnectionFinder();
    connectionFinder.addConnection(inputConnectionView);
    connectionFinder.process();
    this.connectionViews = connectionFinder.getConnections();

    this.subcircuitInstanceViews = new ArrayList<>();
    this.pinViews = new ArrayList<>();
    this.splitterViews = new ArrayList<>();

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
          splitterViews.add(new ComponentSimulationConnection<>((SplitterView) connectedView, subcircuitSimulation, connectionView));
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


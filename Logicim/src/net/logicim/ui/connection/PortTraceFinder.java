package net.logicim.ui.connection;

import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.wire.WireView;
import net.logicim.ui.integratedcircuit.standard.passive.splitter.SplitterView;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PortTraceFinder
{
  protected ConnectionFinder connectionFinder;

  public PortTraceFinder()
  {
    connectionFinder = new ConnectionFinder();
  }

  public void findPorts(ConnectionView connectionView, PortView portView)
  {
    connectionFinder.findConnections(connectionView);
    Set<ConnectionView> connections = connectionFinder.getConnections();

    Set<ComponentView<?>> connectedComponents = new LinkedHashSet<>();
    Set<WireView> connectedWires = new LinkedHashSet<>();
    Set<SplitterView> splitterViews = new LinkedHashSet<>();
    for (ConnectionView connection : connections)
    {
      List<View> localConnected = connection.getConnectedComponents();
      for (View connectedView : localConnected)
      {
        if (connectedView instanceof ComponentView)
        {
          ComponentView<?> componentView = (ComponentView<?>) connectedView;
          if (componentView instanceof SplitterView)
          {
            splitterViews.add((SplitterView) componentView);
          }
          connectedComponents.add(componentView);

        }
        else if (connectedView instanceof WireView)
        {
          connectedWires.add((WireView) connectedView);
        }
      }
    }
  }
}


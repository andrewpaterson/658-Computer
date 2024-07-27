package net.logicim.ui.common.port;

import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.View;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PortViewFinder
{
  public static Set<PortView> findPortViews(Collection<ConnectionView> connectionViews)
  {
    LinkedHashSet<PortView> portViews = new LinkedHashSet<>();
    for (ConnectionView connectionView : connectionViews)
    {
      List<View> connectedComponents = connectionView.getConnectedComponents();
      for (View connectedView : connectedComponents)
      {
        if (connectedView instanceof ComponentView)
        {
          ComponentView<?> componentView = (ComponentView<?>) connectedView;
          PortView portView = componentView.getPortView(connectionView);
          if (portView != null)
          {
            portViews.add(portView);
          }
        }
      }
    }
    return portViews;
  }
}


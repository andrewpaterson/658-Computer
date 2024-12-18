package net.logicim.ui.connection;

import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.*;

public class LocalConnectionNet
{
  protected ViewPath viewPath;
  protected List<ConnectionView> connectionViews;

  protected List<ComponentConnection<SubcircuitInstanceView>> subcircuitInstanceViews;
  protected List<ComponentConnection<PinView>> pinViews;
  protected List<ComponentConnection<SplitterView>> splitterViews;

  public LocalConnectionNet(ViewPath viewPath, ConnectionView inputConnectionView)
  {
    this.viewPath = viewPath;

    HashSet<ConnectionView> connectionViewSet = new HashSet<>();
    HashSet<ComponentConnection<SubcircuitInstanceView>> subcircuitInstanceViewSet = new HashSet<>();
    HashSet<ComponentConnection<PinView>> pinViewSet = new HashSet<>();
    HashSet<ComponentConnection<SplitterView>> splitterViewSet = new HashSet<>();

    process(inputConnectionView,
            connectionViewSet,
            subcircuitInstanceViewSet,
            pinViewSet,
            splitterViewSet);

    this.connectionViews = new ArrayList<>(connectionViewSet);
    Collections.sort(connectionViews);

    this.subcircuitInstanceViews = new ArrayList<>(subcircuitInstanceViewSet);
    Collections.sort(subcircuitInstanceViews);

    this.pinViews = new ArrayList<>(pinViewSet);
    Collections.sort(pinViews);

    this.splitterViews = new ArrayList<>(splitterViewSet);
    Collections.sort(splitterViews);
  }

  private void process(ConnectionView inputConnectionView,
                       Set<ConnectionView> connectionViewSet,
                       Set<ComponentConnection<SubcircuitInstanceView>> subcircuitInstanceViewSet,
                       Set<ComponentConnection<PinView>> pinViewSet,
                       Set<ComponentConnection<SplitterView>> splitterViewSet)
  {
    ConnectionViewNetFinder finder = new ConnectionViewNetFinder();
    finder.addConnectionToProcess(inputConnectionView);
    finder.process();
    connectionViewSet.addAll(finder.getConnections());

    for (ConnectionView connectionView : connectionViewSet)
    {
      List<View> localConnected = connectionView.getConnectedComponents();
      for (View connectedView : localConnected)
      {
        if (connectedView instanceof SubcircuitInstanceView)
        {
          subcircuitInstanceViewSet.add(new ComponentConnection<>(viewPath, (SubcircuitInstanceView) connectedView, connectionView));
        }
        else if (connectedView instanceof SplitterView)
        {
          splitterViewSet.add(new ComponentConnection<>(viewPath, (SplitterView) connectedView, connectionView));
        }
        else if (connectedView instanceof PinView)
        {
          pinViewSet.add(new ComponentConnection<>(viewPath, (PinView) connectedView, connectionView));
        }
      }
    }
  }

  public List<ConnectionView> getConnectionViews()
  {
    return connectionViews;
  }

  public List<ComponentConnection<SplitterView>> getSplitterViews()
  {
    return splitterViews;
  }

  public ViewPath getViewPath()
  {
    return viewPath;
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(viewPath,
                        connectionViews,
                        subcircuitInstanceViews,
                        pinViews,
                        splitterViews);
  }

  public boolean equals(Object obj)
  {
    if (!(obj instanceof LocalConnectionNet))
    {
      return false;
    }

    LocalConnectionNet other = (LocalConnectionNet) obj;
    if (viewPath != other.viewPath)
    {
      return false;
    }

    if (!connectionViews.equals(other.connectionViews))
    {
      return false;
    }

    if (!splitterViews.equals(other.splitterViews))
    {
      return false;
    }

    if (!pinViews.equals(other.pinViews))
    {
      return false;
    }

    if (!subcircuitInstanceViews.equals(other.subcircuitInstanceViews))
    {
      return false;
    }

    return true;
  }
}


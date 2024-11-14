package net.logicim.ui.connection;

import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.ComponentView;

import java.util.Objects;

public class ComponentConnection<T extends ComponentView>
  implements Comparable<ComponentConnection>
{
  protected ViewPath path;
  protected T componentView;
  protected ConnectionView connectionView;

  public ComponentConnection(ViewPath path,
                             T componentView,
                             ConnectionView connectionView)
  {
    this.path = path;
    this.componentView = componentView;
    this.connectionView = connectionView;
  }

  public T getComponentView()
  {
    return componentView;
  }

  public ConnectionView getConnectionView()
  {
    return connectionView;
  }

  public ViewPath getPath()
  {
    return path;
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(path,
                        componentView,
                        connectionView);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (!(obj instanceof ComponentConnection))
    {
      return false;
    }

    ComponentConnection<T> other = (ComponentConnection<T>) obj;
    return equals(other.path,
                  other.componentView,
                  other.connectionView);
  }

  public boolean equals(ViewPath path, T componentView, ConnectionView connectionView)
  {
    return (this.path == path) &&
           (this.componentView == componentView) &&
           (this.connectionView == connectionView);
  }

  @Override
  public int compareTo(ComponentConnection other)
  {
    int result = path.compareTo(other.path);
    if (result != 0)
    {
      return result;
    }

    result = componentView.compareTo(other.componentView);
    if (result != 0)
    {
      return result;
    }

    result = connectionView.compareTo(other.connectionView);
    return result;
  }
}


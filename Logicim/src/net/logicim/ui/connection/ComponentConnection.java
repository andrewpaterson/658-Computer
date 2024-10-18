package net.logicim.ui.connection;

import net.logicim.ui.circuit.CircuitInstanceViewPath;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;

public class ComponentConnection<T extends View>
{
  protected CircuitInstanceViewPath path;
  protected T component;
  protected ConnectionView connection;

  public ComponentConnection(CircuitInstanceViewPath path,
                             T component,
                             ConnectionView connection)
  {
    this.path = path;
    this.component = component;
    this.connection = connection;
  }

  public T getComponent()
  {
    return component;
  }

  public ConnectionView getConnection()
  {
    return connection;
  }

  public CircuitInstanceViewPath getPath()
  {
    return path;
  }
}


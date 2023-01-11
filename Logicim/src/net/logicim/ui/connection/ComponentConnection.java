package net.logicim.ui.connection;

import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;

public class ComponentConnection<T extends View>
{
  protected T component;
  protected ConnectionView connection;

  public ComponentConnection(T component, ConnectionView connection)
  {
    this.component = component;
    this.connection = connection;
  }
}

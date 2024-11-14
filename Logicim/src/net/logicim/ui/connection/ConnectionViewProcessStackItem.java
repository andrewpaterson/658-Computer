package net.logicim.ui.connection;

import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.ConnectionView;

public class ConnectionViewProcessStackItem
{
  public ViewPath viewPath;
  public ConnectionView inputConnectionView;

  public ConnectionViewProcessStackItem(ViewPath viewPath, ConnectionView inputConnectionView)
  {
    this.viewPath = viewPath;
    this.inputConnectionView = inputConnectionView;
  }
}


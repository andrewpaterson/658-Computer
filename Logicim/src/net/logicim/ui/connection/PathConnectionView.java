package net.logicim.ui.connection;

import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.ConnectionView;

public class PathConnectionView
{
  protected ViewPath viewPath;
  protected ConnectionView connection;

  public PathConnectionView(ViewPath viewPath, ConnectionView connection)
  {
    this.viewPath = viewPath;
    this.connection = connection;
  }

  public void clear()
  {
    viewPath = null;
    connection = null;
  }
}


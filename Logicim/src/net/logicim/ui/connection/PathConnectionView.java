package net.logicim.ui.connection;

import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.ConnectionView;

public class PathConnectionView
{
  protected ViewPath path;
  protected ConnectionView connection;

  public PathConnectionView(ViewPath path,
                            ConnectionView connection)
  {
    this.path = path;
    this.connection = connection;
  }

  public void clear()
  {
    path = null;
    connection = null;
  }
}


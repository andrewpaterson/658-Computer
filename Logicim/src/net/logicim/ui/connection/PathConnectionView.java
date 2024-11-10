package net.logicim.ui.connection;

import net.logicim.ui.circuit.path.CircuitInstanceViewPath;
import net.logicim.ui.common.ConnectionView;

public class PathConnectionView
{
  protected CircuitInstanceViewPath path;
  protected ConnectionView connection;

  public PathConnectionView(CircuitInstanceViewPath path,
                            ConnectionView connection)
  {
    this.path = path;
    this.connection = connection;
  }
}


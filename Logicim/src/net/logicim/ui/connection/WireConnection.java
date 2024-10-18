package net.logicim.ui.connection;

import net.logicim.ui.circuit.CircuitInstanceViewPath;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.wire.WireView;

public class WireConnection
{
  protected CircuitInstanceViewPath path;
  protected WireView wireView;
  protected ConnectionView connectionView;

  public WireConnection(CircuitInstanceViewPath path,
                        WireView wireView,
                        ConnectionView connectionView)
  {
    this.path = path;
    this.wireView = wireView;
    this.connectionView = connectionView;
  }

  public WireView getWireView()
  {
    return wireView;
  }

  public CircuitInstanceViewPath getPath()
  {
    return path;
  }
}


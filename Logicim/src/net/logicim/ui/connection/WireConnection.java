package net.logicim.ui.connection;

import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.wire.WireView;

public class WireConnection
{
  protected WireView wireView;
  protected ConnectionView connectionView;

  public WireConnection(WireView wireView, ConnectionView connectionView)
  {
    this.wireView = wireView;
    this.connectionView = connectionView;
  }

  public WireView getWireView()
  {
    return wireView;
  }
}


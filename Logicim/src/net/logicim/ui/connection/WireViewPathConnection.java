package net.logicim.ui.connection;

import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.wire.WireView;

public class WireViewPathConnection
{
  protected ViewPath path;
  protected WireView wireView;
  protected ConnectionView connectionView;

  public WireViewPathConnection(ViewPath path,
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

  public ViewPath getPath()
  {
    return path;
  }
}


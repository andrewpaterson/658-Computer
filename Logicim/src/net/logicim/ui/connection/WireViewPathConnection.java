package net.logicim.ui.connection;

import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.wire.WireView;

public class WireViewPathConnection
{
  protected ViewPath viewPath;
  protected WireView wireView;
  protected ConnectionView connectionView;

  public WireViewPathConnection(ViewPath viewPath,
                                WireView wireView,
                                ConnectionView connectionView)
  {
    this.viewPath = viewPath;
    this.wireView = wireView;
    this.connectionView = connectionView;
  }

  public WireView getWireView()
  {
    return wireView;
  }

  public ViewPath getViewPath()
  {
    return viewPath;
  }
}


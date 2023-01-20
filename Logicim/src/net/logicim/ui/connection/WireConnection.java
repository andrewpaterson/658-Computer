package net.logicim.ui.connection;

import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.wire.WireView;

public class WireConnection
{
  protected WireView component;
  protected ConnectionView connection;

  public WireConnection(WireView component, ConnectionView connection)
  {
    this.component = component;
    this.connection = connection;
  }
}


package net.logicim.ui.connection;

import java.util.List;

public class PartialWire
{
  protected List<WireConnection> connectedWires;

  public PartialWire(List<WireConnection> connectedWires)
  {
    this.connectedWires = connectedWires;
  }
}

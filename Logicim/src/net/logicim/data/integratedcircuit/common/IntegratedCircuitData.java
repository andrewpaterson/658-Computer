package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.port.PortData;
import net.logicim.ui.common.Rotation;

import java.util.List;

public class IntegratedCircuitData
    extends SaveData
{
  protected Int2D position;
  protected Rotation rotation;

  protected List<PortData> portData;

  public IntegratedCircuitData(Int2D position, Rotation rotation, List<PortData> portData)
  {
    this.position = position;
    this.rotation = rotation;
    this.portData = portData;
  }
}


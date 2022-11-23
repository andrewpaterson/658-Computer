package net.logicim.data.integratedcircuit.standard.logic.buffer;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.port.PortData;
import net.logicim.ui.common.Rotation;

import java.util.List;

public class BufferData
    extends IntegratedCircuitData
{
  public BufferData(Int2D position, Rotation rotation, List<PortData> portData)
  {
    super(position, rotation, portData);
  }
}


package net.logicim.data.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.port.PortData;
import net.logicim.ui.common.Rotation;

import java.util.List;

public class NorGateData
    extends LogicGateData
{
  public NorGateData(Int2D position, Rotation rotation, List<PortData> portData, int inputCount)
  {
    super(position, rotation, portData, inputCount);
  }
}


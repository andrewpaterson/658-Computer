package net.logicim.data.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.logic.common.LogicGateData;
import net.logicim.data.port.PortData;
import net.logicim.ui.common.Rotation;

import java.util.List;

public class NandGateData
    extends LogicGateData
{
  public NandGateData(Int2D position, Rotation rotation, List<PortData> portData, int inputCount)
  {
    super(position, rotation, portData, inputCount);
  }
}


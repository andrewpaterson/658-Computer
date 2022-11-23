package net.logicim.data.integratedcircuit.standard.logic.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.port.PortData;
import net.logicim.ui.common.Rotation;

import java.util.List;

public abstract class LogicGateData
    extends IntegratedCircuitData
{
  protected int inputCount;

  public LogicGateData(Int2D position, Rotation rotation, List<PortData> portData, int inputCount)
  {
    super(position, rotation, portData);
    this.inputCount = inputCount;
  }
}


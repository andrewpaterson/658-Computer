package net.logicim.data.integratedcircuit.standard.power;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.DiscreteData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.DiscreteView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.power.GroundView;

public class GroundPortData
    extends DiscreteData
{
  public GroundPortData()
  {
  }

  public GroundPortData(Int2D position, Rotation rotation, String name)
  {
    super(position, rotation, name);
  }

  @Override
  public DiscreteView createAndLoad(CircuitEditor circuitEditor,
                                    TraceLoader traceLoader)
  {
    return new GroundView(circuitEditor, position, rotation, name);
  }
}


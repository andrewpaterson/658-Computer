package net.logicim.ui.integratedcircuit.extra;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.domain.integratedcircuit.extra.Oscilloscope;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.IntegratedCircuitView;
import net.logicim.ui.common.Rotation;

public class OscilloscopeView
    extends IntegratedCircuitView<Oscilloscope>
{
  public OscilloscopeView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, String name)
  {
    super(circuitEditor, position, rotation, name);
  }

  @Override
  protected Oscilloscope createIntegratedCircuit()
  {
    return null;
  }

  @Override
  public IntegratedCircuitData<?> save()
  {
    return null;
  }
}


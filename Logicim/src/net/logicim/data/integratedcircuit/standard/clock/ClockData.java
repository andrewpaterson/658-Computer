package net.logicim.data.integratedcircuit.standard.clock;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.clock.ClockView;

import java.util.List;

public class ClockData
    extends IntegratedCircuitData<ClockView>
{
  protected float frequency;

  protected boolean state;

  public ClockData(Int2D position,
                   Rotation rotation,
                   float frequency,
                   boolean state,
                   List<IntegratedCircuitEventData<?>> events,
                   List<PortData> portData)
  {
    super(position, rotation, events, portData);
    this.frequency = frequency;
    this.state = state;
  }

  @Override
  public ClockView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new ClockView(circuitEditor, position, rotation, frequency);
  }
}


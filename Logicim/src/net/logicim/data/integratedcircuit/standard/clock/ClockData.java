package net.logicim.data.integratedcircuit.standard.clock;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorState;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.clock.ClockView;

import java.util.List;

public class ClockData
    extends IntegratedCircuitData<ClockView, ClockOscillatorState>
{
  protected float frequency;

  public ClockData()
  {
  }

  public ClockData(Int2D position,
                   Rotation rotation,
                   String name,
                   String family,
                   float frequency,
                   List<IntegratedCircuitEventData<?>> events,
                   List<PortData> portData,
                   ClockOscillatorState state)
  {
    super(position,
          rotation,
          name,
          family,
          events,
          portData,
          state);
    this.frequency = frequency;
  }

  @Override
  public ClockView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new ClockView(circuitEditor,
                         position,
                         rotation,
                         name,
                         FamilyStore.getInstance().get(family),
                         frequency);
  }
}

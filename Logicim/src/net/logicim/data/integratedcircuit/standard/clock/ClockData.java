package net.logicim.data.integratedcircuit.standard.clock;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.MultiPortData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorState;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.standard.clock.ClockProperties;
import net.logicim.ui.integratedcircuit.standard.clock.ClockView;

import java.util.List;

public class ClockData
    extends StandardIntegratedCircuitData<ClockView, ClockOscillatorState>
{
  protected float frequency;
  protected boolean inverseOut;

  public ClockData()
  {
  }

  public ClockData(Int2D position,
                   Rotation rotation,
                   String name,
                   String family,
                   float frequency,
                   List<IntegratedCircuitEventData<?>> events,
                   List<MultiPortData> ports,
                   boolean selected,
                   ClockOscillatorState state,
                   boolean inverseOut,
                   boolean explicitPowerPorts)
  {
    super(position,
          rotation,
          name,
          family,
          events,
          ports,
          selected,
          state,
          explicitPowerPorts);
    this.frequency = frequency;
    this.inverseOut = inverseOut;
  }

  @Override
  public ClockView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    return new ClockView(circuitEditor,
                         position,
                         rotation,
                         new ClockProperties(name,
                                             FamilyStore.getInstance().get(family),
                                             explicitPowerPorts,
                                             frequency,
                                             inverseOut));
  }
}


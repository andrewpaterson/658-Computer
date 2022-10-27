package net.logicim.domain.integratedcircuit.standard.clock;

import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.trace.TraceValue;

import java.util.ArrayList;
import java.util.List;

public class ClockOscillator
    extends IntegratedCircuit<ClockOscillatorPins>
{
  public ClockOscillator(String name, ClockOscillatorPins pins)
  {
    super(name, pins);
    pins.getInternalIn().initialise(TraceValue.Unsettled);
    tick(getTimeline().getTime(), new ArrayList<>());
  }

  @Override
  public void tick(long time, List<Port> updatedPorts)
  {
    TraceValue value = pins.getInternalIn().readValue();
    if (value.isHigh())
    {
      pins.getOutput().writeBool(false);
      pins.getInternalOut().writeBool(false);
    }
    else
    {
      pins.getOutput().writeBool(true);
      pins.getInternalOut().writeBool(true);
    }
  }

  @Override
  public String getType()
  {
    return "Clock";
  }
}


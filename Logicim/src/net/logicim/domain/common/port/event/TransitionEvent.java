package net.logicim.domain.common.port.event;

import net.logicim.data.port.event.TransitionEventData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.Voltage;
import net.logicim.domain.common.port.Port;

public class TransitionEvent
    extends PortInputEvent
{
  protected float voltage;

  public TransitionEvent(Port port, float voltage, long time, Timeline timeline)
  {
    super(port, timeline.getTime() + time, timeline);
    this.voltage = voltage;
  }

  public TransitionEvent(Port port, float voltage, long time, long id, Timeline timeline)
  {
    super(port, time, id, timeline);
    this.voltage = voltage;
  }

  @Override
  public void execute(Simulation simulation)
  {
    super.execute(simulation);

    port.transitionEvent(simulation, this);
  }

  @Override
  public TransitionEventData save()
  {
    return new TransitionEventData(time, id, voltage);
  }

  @Override
  public String toShortString()
  {
    return super.toShortString() + " " + Voltage.getVoltageString(voltage);
  }
}


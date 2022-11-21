package net.logicim.domain.common.port.event;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;

public class DriveEvent
    extends PortOutputEvent
{
  protected float voltage;

  public DriveEvent(Port port, long time, float voltage)
  {
    super(port, time);
    this.voltage = voltage;
  }

  @Override
  public void execute(Simulation simulation)
  {
    super.execute(simulation);
    port.driveEvent(simulation, this);
  }

  @Override
  public float getVoltage(long time)
  {
    if (time >= this.time)
    {
      return voltage;
    }
    else
    {
      return Float.NaN;
    }
  }
}


package net.logicim.domain.common.port.event;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Omniport;

public class OmniportSlewEvent
    extends OmniportEvent
{
  protected float[] startVoltages;  // @ time
  protected float[] endVoltages;    // @ time + slewTime
  protected long[] slewTimes;

  public OmniportSlewEvent(Omniport port, float[] startVoltages, float[] endVoltages, long[] slewTimes, long time)
  {
    super(port, time);

    this.startVoltages = startVoltages;
    this.endVoltages = endVoltages;
    this.slewTimes = slewTimes;
  }

  @Override
  public void execute(Simulation simulation)
  {
  }

  public long getEndTime(int busIndex)
  {
    return time + slewTimes[busIndex];
  }

  public float getEndVoltage(int busIndex)
  {
    return endVoltages[busIndex];
  }

  public float[] getEndVoltages()
  {
    return endVoltages;
  }
}


package net.logicim.domain.common.port;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.trace.TraceNet;

public class PowerInPort
    extends Port
{
  public PowerInPort(PortType portType, String name, Pins pins)
  {
    super(portType, name, pins);
  }

  @Override
  public String toDebugString()
  {
    return getHolder().getName() + "." + name;
  }

  @Override
  public String getDescription()
  {
    return getHolder().getDescription() + "." + getName();
  }

  @Override
  public void reset()
  {
  }

  @Override
  public boolean isPowerIn()
  {
    return true;
  }

  public float getVoltageIn(long time)
  {
    TraceNet trace = getTrace();
    if (trace != null)
    {
      return trace.getVoltage(time);
    }
    else
    {
      return Float.NaN;
    }
  }
}

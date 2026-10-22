package net.logicim.domain.common.port;

import net.logicim.domain.common.wire.Trace;

public class PowerInPort
    extends Port
{
  public PowerInPort(PortType portType,
                     String name,
                     PortHolder portHolder)
  {
    super(portType, name, portHolder);
  }

  @Override
  public boolean isPowerIn()
  {
    return true;
  }

  public float getVoltageIn(long time)
  {
    Trace trace = getTrace();
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


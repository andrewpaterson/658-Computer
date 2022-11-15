package net.logicim.domain.common.port.event;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;

import java.util.Set;

public class SlewEvent
    extends PortEvent
{
  protected float startVoltage;  // @ time
  protected float endVoltage;    // @ time + slewTime
  protected long slewTime;

  public SlewEvent(Port port, float startVoltage, float endVoltage, long slewTime, long time)
  {
    super(port, time);
    this.startVoltage = startVoltage;
    this.endVoltage = endVoltage;
    this.slewTime = slewTime;
  }

  @Override
  public void execute(Simulation simulation)
  {
    Set<Port> connectedPorts = port.getConnectedPorts();
    for (Port connectedPort : connectedPorts)
    {
      if (connectedPort != port)
      {
        connectedPort.voltageChanging(simulation, startVoltage, endVoltage, slewTime);
      }
    }
  }

  public long getEndTime()
  {
    return time + slewTime;
  }

  public float getEndVoltage()
  {
    return endVoltage;
  }

  public float getVoltage(long time)
  {
    if (time <= getEndTime() && time >= this.time)
    {
      long l = time - this.time;
      float fractionStart = l / (float) slewTime;
      return fractionStart * startVoltage + (1 - fractionStart) * endVoltage;
    }
    else
    {
      throw new SimulatorException("Cannot get voltage outside of slew time for slew event.");
    }
  }
}


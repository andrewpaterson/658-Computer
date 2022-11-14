package net.logicim.domain.common.port.event;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.Uniport;

import java.util.Set;

public class UniportSlewEvent
    extends UniportEvent
{
  protected float startVoltage;  // @ time
  protected float endVoltage;    // @ time + slewTime
  protected long slewTime;

  public UniportSlewEvent(Uniport port, float startVoltage, float endVoltage, long slewTime, long time)
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
        connectedPort.voltageChanging(simulation, port.getTrace(), startVoltage, endVoltage, slewTime);
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
}


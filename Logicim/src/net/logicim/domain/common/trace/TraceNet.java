package net.logicim.domain.common.trace;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.port.Drive;
import net.logicim.domain.common.port.Port;

import java.util.LinkedHashSet;
import java.util.Set;

public class TraceNet
{
  protected Set<Port> connectedPorts;

  public TraceNet()
  {
    connectedPorts = new LinkedHashSet<>();
  }

  public float getDrivenVoltage()
  {
    float drivenVoltage = 0;
    int drivers = 0;
    for (Port port : connectedPorts)
    {
      Drive drive = port.getDrive(this);
      if (drive.isDriven())
      {
        drivenVoltage += drive.getVoltage();
        drivers++;
      }
    }
    if (drivers == 1)
    {
      return drivenVoltage;
    }
    if (drivers == 0)
    {
      throw new SimulatorException("Trace is not driven.");
    }

    return drivenVoltage / drivers;
  }

  public boolean isDriven()
  {
    for (Port port : connectedPorts)
    {
      if (port.isDriven(this))
      {
        return true;
      }
    }
    return false;
  }

  public float getShortVoltage()
  {
    float minimumVoltage = 10000;
    float maximumVoltage = -10000;
    for (Port port : connectedPorts)
    {
      Drive drive = port.getDrive(this);
      if (drive.isDriven())
      {
        float drivenVoltage = drive.getVoltage();
        if (drivenVoltage < minimumVoltage)
        {
          minimumVoltage = drivenVoltage;
        }
        if (drivenVoltage > maximumVoltage)
        {
          maximumVoltage = drivenVoltage;
        }
      }
    }
    if (minimumVoltage == 10000 || maximumVoltage == -10000)
    {
      return 0;
    }
    else
    {
      return maximumVoltage - minimumVoltage;
    }
  }

  public void connect(Port port)
  {
    connectedPorts.add(port);
  }

  public void disconnect(Port port)
  {
    connectedPorts.remove(port);
  }
}


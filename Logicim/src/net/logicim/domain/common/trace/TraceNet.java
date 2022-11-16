package net.logicim.domain.common.trace;

import net.logicim.common.SimulatorException;
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

  public float getVoltage(long time)
  {
    float drivenVoltage = 0;
    int drivers = 0;
    for (Port port : connectedPorts)
    {
      float voltage = port.getVoltage(time);
      if (!Float.isNaN(voltage))
      {
        drivenVoltage += voltage;
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

  public float getShortVoltage(long time)
  {
    float minimumVoltage = 10000;
    float maximumVoltage = -10000;
    for (Port port : connectedPorts)
    {
      float voltage = port.getVoltage(time);
      if (!Float.isNaN(voltage))
      {
        if (voltage < minimumVoltage)
        {
          minimumVoltage = voltage;
        }
        if (voltage > maximumVoltage)
        {
          maximumVoltage = voltage;
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

  public Set<Port> getConnectedPorts()
  {
    return connectedPorts;
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


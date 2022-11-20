package net.logicim.domain.common.trace;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Voltage;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.PortOutputEvent;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
      return Float.NaN;
    }

    return drivenVoltage / drivers;
  }

  public String getVoltageString(long time)
  {
    return Voltage.getVoltageString(getVoltage(time));
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

  public List<PortOutputEvent> getOutputEvents()
  {
    ArrayList<PortOutputEvent> result = new ArrayList<>();
    for (Port port : connectedPorts)
    {
      PortOutputEvent output = port.getOutput();
      if (output != null)
      {
        result.add(output);
      }
    }
    return result;
  }
}


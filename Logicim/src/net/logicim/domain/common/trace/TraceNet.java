package net.logicim.domain.common.trace;

import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PowerOutPort;
import net.logicim.domain.common.port.event.PortOutputEvent;
import net.logicim.domain.common.voltage.Voltage;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TraceNet
    implements Voltage
{
  public static long nextId = 1L;

  protected Set<Port> connectedPorts;
  protected long id;

  public TraceNet()
  {
    connectedPorts = new LinkedHashSet<>();
    id = nextId;
    nextId++;
  }

  public TraceNet(long id)
  {
    connectedPorts = new LinkedHashSet<>();
    this.id = id;
    if (id >= nextId)
    {
      nextId = id + 1;
    }
  }

  public static long getId(TraceNet trace)
  {
    if (trace != null)
    {
      return trace.getId();
    }
    else
    {
      return 0;
    }
  }

  public float getVoltage(long time)
  {
    float drivenVoltage = 0;
    int drivers = 0;
    for (Port port : connectedPorts)
    {
      float voltage = Float.NaN;
      if (port.isLogicPort())
      {
        voltage = ((LogicPort) port).getVoltageOut(time);
      }
      else if (port.isPowerOut())
      {
        voltage = ((PowerOutPort) port).getVoltageOut();
      }
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

  public String getDescription()
  {
    return "Trace";
  }

  public String getVoltageString(long time)
  {
    return Voltage.toVoltageString(getVoltage(time));
  }

  public float getShortVoltage(long time)
  {
    float minimumVoltage = 10000;
    float maximumVoltage = -10000;
    for (Port port : connectedPorts)
    {
      float voltage = Float.NaN;
      if (port.isLogicPort())
      {
        voltage = ((LogicPort) port).getVoltageOut(time);
      }
      else if (port.isPowerOut())
      {
        voltage = ((PowerOutPort) port).getVoltageOut();
      }

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

  public float getMinimumVoltage(long time)
  {
    float minimumVoltage = 10000;
    for (Port port : connectedPorts)
    {
      float voltage = Float.NaN;
      if (port.isLogicPort())
      {
        voltage = ((LogicPort) port).getVoltageOut(time);
      }
      else if (port.isPowerOut())
      {
        voltage = ((PowerOutPort) port).getVoltageOut();
      }

      if (!Float.isNaN(voltage))
      {
        if (voltage < minimumVoltage)
        {
          minimumVoltage = voltage;
        }
      }
    }
    if (minimumVoltage == 10000)
    {
      return Float.NaN;
    }
    else
    {
      return minimumVoltage;
    }
  }

  public float getMaximumVoltage(long time)
  {
    float maximumVoltage = -10000;
    for (Port port : connectedPorts)
    {
      float voltage = Float.NaN;
      if (port.isLogicPort())
      {
        voltage = ((LogicPort) port).getVoltageOut(time);
      }
      else if (port.isPowerOut())
      {
        voltage = ((PowerOutPort) port).getVoltageOut();
      }

      if (!Float.isNaN(voltage))
      {
        if (voltage > maximumVoltage)
        {
          maximumVoltage = voltage;
        }
      }
    }
    if (maximumVoltage == -10000)
    {
      return Float.NaN;
    }
    else
    {
      return maximumVoltage;
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
      if (port.isLogicPort())
      {
        PortOutputEvent output = ((LogicPort) port).getOutput();
        if (output != null)
        {
          result.add(output);
        }
      }
    }
    return result;
  }

  public long getId()
  {
    return id;
  }

  public static void resetNextId()
  {
    nextId = 1L;
  }
}


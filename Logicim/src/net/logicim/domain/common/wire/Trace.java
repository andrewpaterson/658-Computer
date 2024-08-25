package net.logicim.domain.common.wire;

import net.common.SimulatorException;
import net.logicim.domain.common.Described;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.PortOutputEvent;
import net.logicim.domain.common.voltage.Voltage;
import net.logicim.ui.simulation.DebugGlobalEnvironment;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static net.logicim.domain.common.port.OutputPortHelper.getPortOutputVoltage;

public class Trace
    implements Voltage,
               Described
{
  public static long nextId = 1L;

  protected Set<Port> connectedPorts;
  protected long id;

  public Trace()
  {
    this(nextId++);
  }

  public Trace(long id)
  {
    DebugGlobalEnvironment.validateCanCreateTrace();
    connectedPorts = new LinkedHashSet<>();
    this.id = id;
    if (id >= nextId)
    {
      nextId = id + 1;
    }
  }

  public static long getId(Trace trace)
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
      float voltage = getPortOutputVoltage(port, time);

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
      float voltage = getPortOutputVoltage(port, time);

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
      float voltage = getPortOutputVoltage(port, time);

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
      float voltage = getPortOutputVoltage(port, time);

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

